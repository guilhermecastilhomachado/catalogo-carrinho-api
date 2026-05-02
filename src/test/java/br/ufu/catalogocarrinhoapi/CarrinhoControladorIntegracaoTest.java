package br.ufu.catalogocarrinhoapi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class CarrinhoControladorIntegracaoTest {

	@LocalServerPort
	private int port;

	@Test
	void deveExecutarFluxoCompletoDeCarrinho() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		RestClient client = restClient();

		String categoriaJson = """
				{
				  "nome": "Eletronicos",
				  "descricao": "Produtos eletronicos em geral"
				}
				""";

		ResponseEntity<String> categoriaResp = postJson("/categorias", categoriaJson);
		assertEquals(HttpStatus.CREATED, categoriaResp.getStatusCode());
		JsonNode categoriaNode = objectMapper.readTree(categoriaResp.getBody());
		long categoriaId = categoriaNode.get("id").asLong();

		String produtoJson = """
				{
				  "nome": "Mouse Gamer",
				  "descricao": "Mouse com sensor optico e 6 botoes",
				  "preco": 149.90,
				  "quantidadeEstoque": 20,
				  "categoria": {
					"id": %d
				  }
				}
				""".formatted(categoriaId);

		ResponseEntity<String> produtoResp = postJson("/produtos", produtoJson);
		assertEquals(HttpStatus.CREATED, produtoResp.getStatusCode());
		JsonNode produtoNode = objectMapper.readTree(produtoResp.getBody());
		long produtoId = produtoNode.get("id").asLong();

		ResponseEntity<String> carrinhoResp = client.post()
				.uri("/carrinhos")
				.retrieve()
				.toEntity(String.class);
		assertEquals(HttpStatus.CREATED, carrinhoResp.getStatusCode());
		JsonNode carrinhoNode = objectMapper.readTree(carrinhoResp.getBody());
		long carrinhoId = carrinhoNode.get("id").asLong();
		assertEquals("ABERTO", carrinhoNode.get("status").asText());

		String itemJson = """
				{
				  "produtoId": %d,
				  "quantidade": 2
				}
				""".formatted(produtoId);

		ResponseEntity<String> adicionarItemResp = postJson("/carrinhos/%d/itens".formatted(carrinhoId), itemJson);
		assertEquals(HttpStatus.OK, adicionarItemResp.getStatusCode());
		JsonNode carrinhoAposItemNode = objectMapper.readTree(adicionarItemResp.getBody());
		assertEquals(0, carrinhoAposItemNode.get("valorTotal").decimalValue().compareTo(new java.math.BigDecimal("299.80")));

		ResponseEntity<String> checkoutResp = client.post()
				.uri("/carrinhos/%d/checkout".formatted(carrinhoId))
				.retrieve()
				.toEntity(String.class);
		assertEquals(HttpStatus.OK, checkoutResp.getStatusCode());
		JsonNode carrinhoFinalNode = objectMapper.readTree(checkoutResp.getBody());
		assertEquals("FINALIZADO", carrinhoFinalNode.get("status").asText());

		ResponseEntity<String> produtoAtualizadoResp = client.get()
				.uri("/produtos/%d".formatted(produtoId))
				.retrieve()
				.toEntity(String.class);
		assertEquals(HttpStatus.OK, produtoAtualizadoResp.getStatusCode());
		JsonNode produtoAtualizadoNode = objectMapper.readTree(produtoAtualizadoResp.getBody());
		assertEquals(18, produtoAtualizadoNode.get("quantidadeEstoque").asInt());
	}

	@Test
	void deveRetornarErroAoFinalizarCarrinhoVazio() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		RestClient client = restClient();

		ResponseEntity<String> carrinhoResp = client.post()
				.uri("/carrinhos")
				.retrieve()
				.toEntity(String.class);
		assertEquals(HttpStatus.CREATED, carrinhoResp.getStatusCode());

		JsonNode carrinhoNode = objectMapper.readTree(carrinhoResp.getBody());
		long carrinhoId = carrinhoNode.get("id").asLong();

		ResponseEntity<String> checkoutResp = client.post()
				.uri("/carrinhos/%d/checkout".formatted(carrinhoId))
				.exchange((request, response) -> {
					String body = new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8);
					return ResponseEntity.status(response.getStatusCode()).body(body);
				});
		assertEquals(HttpStatus.BAD_REQUEST, checkoutResp.getStatusCode());

		JsonNode erroNode = objectMapper.readTree(checkoutResp.getBody());
		assertEquals("Regra de negocio violada", erroNode.get("erro").asText());
		assertEquals("Nao e possivel finalizar um carrinho vazio.", erroNode.get("mensagem").asText());
	}

	private ResponseEntity<String> postJson(String url, String jsonBody) {
		RestClient client = restClient();
		return client.method(HttpMethod.POST)
				.uri(url)
				.contentType(MediaType.APPLICATION_JSON)
				.body(jsonBody)
				.retrieve()
				.toEntity(String.class);
	}

	private RestClient restClient() {
		return RestClient.builder()
				.baseUrl("http://localhost:" + port)
				.build();
	}
}


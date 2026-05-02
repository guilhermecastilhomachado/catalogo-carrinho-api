package br.ufu.catalogocarrinhoapi.servico;

import br.ufu.catalogocarrinhoapi.excecao.RecursoNaoEncontradoExcecao;
import br.ufu.catalogocarrinhoapi.modelo.Categoria;
import br.ufu.catalogocarrinhoapi.repositorio.CategoriaRepositorio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoriaServicoTest {

	@Mock
	private CategoriaRepositorio categoriaRepositorio;

	@InjectMocks
	private CategoriaServico categoriaServico;

	@Test
	void deveCriarCategoriaComSucesso() {
		Categoria categoria = new Categoria();
		categoria.setNome("Eletronicos");
		categoria.setDescricao("Produtos eletronicos em geral");

		when(categoriaRepositorio.save(any(Categoria.class))).thenAnswer(invocacao -> {
			Categoria categoriaSalva = invocacao.getArgument(0);
			categoriaSalva.setId(1L);
			return categoriaSalva;
		});

		Categoria resultado = categoriaServico.criarCategoria(categoria);

		assertNotNull(resultado);
		assertEquals(1L, resultado.getId());
		assertEquals("Eletronicos", resultado.getNome());

		verify(categoriaRepositorio, times(1)).save(categoria);
	}

	@Test
	void deveLancarExcecaoQuandoCategoriaNaoExistir() {
		when(categoriaRepositorio.findById(999L)).thenReturn(Optional.empty());

		RecursoNaoEncontradoExcecao excecao = assertThrows(
				RecursoNaoEncontradoExcecao.class,
				() -> categoriaServico.buscarCategoriaPorId(999L)
		);

		assertEquals("Categoria nao encontrada com id: 999", excecao.getMessage());
		verify(categoriaRepositorio, times(1)).findById(999L);
	}
}


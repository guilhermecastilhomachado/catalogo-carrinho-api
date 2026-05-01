package br.ufu.catalogocarrinhoapi.controlador;

import br.ufu.catalogocarrinhoapi.dto.AtualizarQuantidadeItemDto;
import br.ufu.catalogocarrinhoapi.dto.ItemCarrinhoRequisicaoDto;
import br.ufu.catalogocarrinhoapi.modelo.Carrinho;
import br.ufu.catalogocarrinhoapi.servico.CarrinhoServico;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carrinhos")
@RequiredArgsConstructor
public class CarrinhoControlador {

    private final CarrinhoServico carrinhoServico;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Carrinho criarCarrinho() {
        return carrinhoServico.criarCarrinho();
    }

    @GetMapping
    public List<Carrinho> listarCarrinhos() {
        return carrinhoServico.listarCarrinhos();
    }

    @GetMapping("/{id}")
    public Carrinho buscarCarrinhoPorId(@PathVariable Long id) {
        return carrinhoServico.buscarCarrinhoPorId(id);
    }

    @PostMapping("/{carrinhoId}/itens")
    public Carrinho adicionarItem(@PathVariable Long carrinhoId,
                                  @RequestBody @Valid ItemCarrinhoRequisicaoDto dto) {
        return carrinhoServico.adicionarItem(carrinhoId, dto);
    }

    @PutMapping("/{carrinhoId}/itens/{itemId}")
    public Carrinho atualizarQuantidadeItem(@PathVariable Long carrinhoId,
                                            @PathVariable Long itemId,
                                            @RequestBody @Valid AtualizarQuantidadeItemDto dto) {
        return carrinhoServico.atualizarQuantidadeItem(carrinhoId, itemId, dto);
    }

    @DeleteMapping("/{carrinhoId}/itens/{itemId}")
    public Carrinho removerItem(@PathVariable Long carrinhoId,
                                @PathVariable Long itemId) {
        return carrinhoServico.removerItem(carrinhoId, itemId);
    }

    @PostMapping("/{carrinhoId}/checkout")
    public Carrinho finalizarCarrinho(@PathVariable Long carrinhoId) {
        return carrinhoServico.finalizarCarrinho(carrinhoId);
    }
}
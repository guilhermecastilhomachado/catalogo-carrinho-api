package br.ufu.catalogocarrinhoapi.servico;

import br.ufu.catalogocarrinhoapi.dto.ItemCarrinhoRequisicaoDto;
import br.ufu.catalogocarrinhoapi.enumeracao.StatusCarrinho;
import br.ufu.catalogocarrinhoapi.modelo.Carrinho;
import br.ufu.catalogocarrinhoapi.modelo.ItemCarrinho;
import br.ufu.catalogocarrinhoapi.modelo.Produto;
import br.ufu.catalogocarrinhoapi.repositorio.CarrinhoRepositorio;
import br.ufu.catalogocarrinhoapi.repositorio.ItemCarrinhoRepositorio;
import br.ufu.catalogocarrinhoapi.repositorio.ProdutoRepositorio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarrinhoServicoTest {

    @Mock
    private CarrinhoRepositorio carrinhoRepositorio;

    @Mock
    private ItemCarrinhoRepositorio itemCarrinhoRepositorio;

    @Mock
    private ProdutoServico produtoServico;

    @Mock
    private ProdutoRepositorio produtoRepositorio;

    @InjectMocks
    private CarrinhoServico carrinhoServico;

    @Test
    void deveAdicionarItemAoCarrinhoERecalcularTotal() {
        Carrinho carrinho = new Carrinho();
        carrinho.setId(1L);
        carrinho.setStatus(StatusCarrinho.ABERTO);
        carrinho.setValorTotal(BigDecimal.ZERO);
        carrinho.setItens(new ArrayList<>());

        Produto produto = new Produto();
        produto.setId(1L);
        produto.setNome("Mouse Gamer");
        produto.setPreco(new BigDecimal("149.90"));
        produto.setQuantidadeEstoque(20);

        ItemCarrinhoRequisicaoDto dto = new ItemCarrinhoRequisicaoDto();
        dto.setProdutoId(1L);
        dto.setQuantidade(2);

        when(carrinhoRepositorio.findById(1L)).thenReturn(Optional.of(carrinho));
        when(produtoServico.buscarProdutoPorId(1L)).thenReturn(produto);
        when(itemCarrinhoRepositorio.save(any(ItemCarrinho.class))).thenAnswer(invocacao -> {
            ItemCarrinho item = invocacao.getArgument(0);
            item.setId(10L);
            item.setSubtotal(item.getPrecoUnitario().multiply(BigDecimal.valueOf(item.getQuantidade())));
            return item;
        });
        when(carrinhoRepositorio.save(any(Carrinho.class))).thenAnswer(invocacao -> invocacao.getArgument(0));

        Carrinho resultado = carrinhoServico.adicionarItem(1L, dto);

        assertNotNull(resultado);
        assertEquals(1, resultado.getItens().size());
        assertEquals(0, resultado.getValorTotal().compareTo(new BigDecimal("299.80")));
    }

    @Test
    void deveFinalizarCarrinhoEDebitarEstoque() {
        Produto produto = new Produto();
        produto.setId(1L);
        produto.setNome("Mouse Gamer");
        produto.setPreco(new BigDecimal("149.90"));
        produto.setQuantidadeEstoque(20);

        ItemCarrinho item = new ItemCarrinho();
        item.setId(1L);
        item.setProduto(produto);
        item.setQuantidade(2);
        item.setPrecoUnitario(new BigDecimal("149.90"));
        item.setSubtotal(new BigDecimal("299.80"));

        Carrinho carrinho = new Carrinho();
        carrinho.setId(1L);
        carrinho.setStatus(StatusCarrinho.ABERTO);
        carrinho.setValorTotal(new BigDecimal("299.80"));
        carrinho.setItens(new ArrayList<>());
        carrinho.getItens().add(item);

        when(carrinhoRepositorio.findById(1L)).thenReturn(Optional.of(carrinho));
        when(produtoRepositorio.save(any(Produto.class))).thenAnswer(invocacao -> invocacao.getArgument(0));
        when(carrinhoRepositorio.save(any(Carrinho.class))).thenAnswer(invocacao -> invocacao.getArgument(0));

        Carrinho resultado = carrinhoServico.finalizarCarrinho(1L);

        assertEquals(StatusCarrinho.FINALIZADO, resultado.getStatus());
        assertEquals(18, produto.getQuantidadeEstoque());
    }
}

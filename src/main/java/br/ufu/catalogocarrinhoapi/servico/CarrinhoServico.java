package br.ufu.catalogocarrinhoapi.servico;

import br.ufu.catalogocarrinhoapi.dto.AtualizarQuantidadeItemDto;
import br.ufu.catalogocarrinhoapi.dto.ItemCarrinhoRequisicaoDto;
import br.ufu.catalogocarrinhoapi.enumeracao.StatusCarrinho;
import br.ufu.catalogocarrinhoapi.excecao.RecursoNaoEncontradoExcecao;
import br.ufu.catalogocarrinhoapi.modelo.Carrinho;
import br.ufu.catalogocarrinhoapi.modelo.ItemCarrinho;
import br.ufu.catalogocarrinhoapi.modelo.Produto;
import br.ufu.catalogocarrinhoapi.repositorio.CarrinhoRepositorio;
import br.ufu.catalogocarrinhoapi.repositorio.ItemCarrinhoRepositorio;
import br.ufu.catalogocarrinhoapi.repositorio.ProdutoRepositorio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CarrinhoServico {

    private final CarrinhoRepositorio carrinhoRepositorio;
    private final ItemCarrinhoRepositorio itemCarrinhoRepositorio;
    private final ProdutoServico produtoServico;
    private final ProdutoRepositorio produtoRepositorio;

    public Carrinho criarCarrinho() {
        Carrinho carrinho = new Carrinho();
        carrinho.setStatus(StatusCarrinho.ABERTO);
        carrinho.setValorTotal(BigDecimal.ZERO);
        return carrinhoRepositorio.save(carrinho);
    }

    public List<Carrinho> listarCarrinhos() {
        return carrinhoRepositorio.findAll();
    }

    public Carrinho buscarCarrinhoPorId(Long id) {
        return carrinhoRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoExcecao("Carrinho nao encontrado com id: " + id));
    }

    public Carrinho adicionarItem(Long carrinhoId, ItemCarrinhoRequisicaoDto dto) {
        Carrinho carrinho = buscarCarrinhoPorId(carrinhoId);
        validarCarrinhoAberto(carrinho);

        Produto produto = produtoServico.buscarProdutoPorId(dto.getProdutoId());

        if (dto.getQuantidade() > produto.getQuantidadeEstoque()) {
            throw new IllegalArgumentException("Quantidade solicitada maior que o estoque disponivel.");
        }

        Optional<ItemCarrinho> itemExistenteOpt = carrinho.getItens().stream()
                .filter(item -> item.getProduto().getId().equals(produto.getId()))
                .findFirst();

        if (itemExistenteOpt.isPresent()) {
            ItemCarrinho itemExistente = itemExistenteOpt.get();
            int novaQuantidade = itemExistente.getQuantidade() + dto.getQuantidade();

            if (novaQuantidade > produto.getQuantidadeEstoque()) {
                throw new IllegalArgumentException("Quantidade total no carrinho maior que o estoque disponivel.");
            }

            itemExistente.setQuantidade(novaQuantidade);
            itemExistente.setPrecoUnitario(produto.getPreco());
            itemCarrinhoRepositorio.save(itemExistente);
        } else {
            ItemCarrinho novoItem = ItemCarrinho.builder()
                    .produto(produto)
                    .quantidade(dto.getQuantidade())
                    .precoUnitario(produto.getPreco())
                    .carrinho(carrinho)
                    .build();

            itemCarrinhoRepositorio.save(novoItem);
            carrinho.getItens().add(novoItem);
        }

        recalcularValorTotal(carrinho);
        return carrinhoRepositorio.save(carrinho);
    }

    public Carrinho atualizarQuantidadeItem(Long carrinhoId, Long itemId, AtualizarQuantidadeItemDto dto) {
        Carrinho carrinho = buscarCarrinhoPorId(carrinhoId);
        validarCarrinhoAberto(carrinho);

        ItemCarrinho item = buscarItemDoCarrinho(carrinho, itemId);
        Produto produto = item.getProduto();

        if (dto.getQuantidade() > produto.getQuantidadeEstoque()) {
            throw new IllegalArgumentException("Quantidade solicitada maior que o estoque disponivel.");
        }

        item.setQuantidade(dto.getQuantidade());
        item.setPrecoUnitario(produto.getPreco());
        itemCarrinhoRepositorio.save(item);

        recalcularValorTotal(carrinho);
        return carrinhoRepositorio.save(carrinho);
    }

    public Carrinho removerItem(Long carrinhoId, Long itemId) {
        Carrinho carrinho = buscarCarrinhoPorId(carrinhoId);
        validarCarrinhoAberto(carrinho);

        ItemCarrinho item = buscarItemDoCarrinho(carrinho, itemId);

        carrinho.getItens().removeIf(i -> i.getId().equals(itemId));
        itemCarrinhoRepositorio.delete(item);

        recalcularValorTotal(carrinho);
        return carrinhoRepositorio.save(carrinho);
    }

    public Carrinho finalizarCarrinho(Long carrinhoId) {
        Carrinho carrinho = buscarCarrinhoPorId(carrinhoId);
        validarCarrinhoAberto(carrinho);

        if (carrinho.getItens().isEmpty()) {
            throw new IllegalArgumentException("Nao e possivel finalizar um carrinho vazio.");
        }

        for (ItemCarrinho item : carrinho.getItens()) {
            Produto produto = item.getProduto();

            if (item.getQuantidade() > produto.getQuantidadeEstoque()) {
                throw new IllegalArgumentException("Estoque insuficiente para o produto: " + produto.getNome());
            }

            produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() - item.getQuantidade());
            produtoRepositorio.save(produto);
        }

        carrinho.setStatus(StatusCarrinho.FINALIZADO);
        recalcularValorTotal(carrinho);

        return carrinhoRepositorio.save(carrinho);
    }

    private ItemCarrinho buscarItemDoCarrinho(Carrinho carrinho, Long itemId) {
        return carrinho.getItens().stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new RecursoNaoEncontradoExcecao(
                        "Item do carrinho nao encontrado com id: " + itemId));
    }

    private void validarCarrinhoAberto(Carrinho carrinho) {
        if (carrinho.getStatus() == StatusCarrinho.FINALIZADO) {
            throw new IllegalArgumentException("Nao e permitido alterar um carrinho finalizado.");
        }
    }

    private void recalcularValorTotal(Carrinho carrinho) {
        BigDecimal total = carrinho.getItens().stream()
                .map(ItemCarrinho::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        carrinho.setValorTotal(total);
    }
}
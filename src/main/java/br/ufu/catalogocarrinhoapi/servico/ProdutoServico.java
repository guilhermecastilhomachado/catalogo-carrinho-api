package br.ufu.catalogocarrinhoapi.servico;

import br.ufu.catalogocarrinhoapi.excecao.RecursoNaoEncontradoExcecao;
import br.ufu.catalogocarrinhoapi.modelo.Categoria;
import br.ufu.catalogocarrinhoapi.modelo.Produto;
import br.ufu.catalogocarrinhoapi.repositorio.ProdutoRepositorio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


@Service
@RequiredArgsConstructor
public class ProdutoServico {

    private final ProdutoRepositorio produtoRepositorio;
    private final CategoriaServico categoriaServico;

    public Produto criarProduto(Produto produto) {
        Long categoriaId = produto.getCategoria().getId();
        Categoria categoria = categoriaServico.buscarCategoriaPorId(categoriaId);
        produto.setCategoria(categoria);

        return produtoRepositorio.save(produto);
    }

    public Produto buscarProdutoPorId(Long id) {
        return produtoRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoExcecao("Produto nao encontrado com id: " + id));
    }

    public Produto atualizarProduto(Long id, Produto produtoAtualizado) {
        Produto produtoExistente = buscarProdutoPorId(id);

        Long categoriaId = produtoAtualizado.getCategoria().getId();
        Categoria categoria = categoriaServico.buscarCategoriaPorId(categoriaId);

        produtoExistente.setNome(produtoAtualizado.getNome());
        produtoExistente.setDescricao(produtoAtualizado.getDescricao());
        produtoExistente.setPreco(produtoAtualizado.getPreco());
        produtoExistente.setQuantidadeEstoque(produtoAtualizado.getQuantidadeEstoque());
        produtoExistente.setCategoria(categoria);

        return produtoRepositorio.save(produtoExistente);
    }

    public void removerProduto(Long id) {
        Produto produto = buscarProdutoPorId(id);
        produtoRepositorio.delete(produto);
    }

    public Page<Produto> listarProdutosPaginados(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        return produtoRepositorio.findAll(pageable);
    }

    public Page<Produto> listarProdutosPorCategoriaPaginados(Long categoriaId, int page, int size, String sortBy, String direction) {
        categoriaServico.buscarCategoriaPorId(categoriaId);

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        return produtoRepositorio.findByCategoriaId(categoriaId, pageable);
    }
}
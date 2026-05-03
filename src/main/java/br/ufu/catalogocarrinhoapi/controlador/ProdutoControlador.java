package br.ufu.catalogocarrinhoapi.controlador;

import br.ufu.catalogocarrinhoapi.modelo.Produto;
import br.ufu.catalogocarrinhoapi.servico.ProdutoServico;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;


@RestController
@RequestMapping("/produtos")
@RequiredArgsConstructor
public class ProdutoControlador {

    private final ProdutoServico produtoServico;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Produto criarProduto(@RequestBody @Valid Produto produto) {
        return produtoServico.criarProduto(produto);
    }

    @GetMapping
    public Page<Produto> listarProdutos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nome") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        return produtoServico.listarProdutosPaginados(page, size, sortBy, direction);
    }
    @GetMapping("/{id}")
    public Produto buscarProdutoPorId(@PathVariable Long id) {
        return produtoServico.buscarProdutoPorId(id);
    }

    @PutMapping("/{id}")
    public Produto atualizarProduto(@PathVariable Long id, @RequestBody @Valid Produto produto) {
        return produtoServico.atualizarProduto(id, produto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removerProduto(@PathVariable Long id) {
        produtoServico.removerProduto(id);
    }

    @GetMapping("/categoria/{categoriaId}")
    public Page<Produto> listarProdutosPorCategoria(
            @PathVariable Long categoriaId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nome") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        return produtoServico.listarProdutosPorCategoriaPaginados(categoriaId, page, size, sortBy, direction);
    }
}
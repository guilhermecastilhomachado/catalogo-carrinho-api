package br.ufu.catalogocarrinhoapi.controlador;

import br.ufu.catalogocarrinhoapi.modelo.Produto;
import br.ufu.catalogocarrinhoapi.servico.ProdutoServico;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public List<Produto> listarProdutos() {
        return produtoServico.listarProdutos();
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
    public List<Produto> listarProdutosPorCategoria(@PathVariable Long categoriaId) {
        return produtoServico.listarProdutosPorCategoria(categoriaId);
    }
}
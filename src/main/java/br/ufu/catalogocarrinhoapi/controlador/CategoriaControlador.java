package br.ufu.catalogocarrinhoapi.controlador;

import br.ufu.catalogocarrinhoapi.modelo.Categoria;
import br.ufu.catalogocarrinhoapi.servico.CategoriaServico;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorias")
@RequiredArgsConstructor
public class CategoriaControlador {

    private final CategoriaServico categoriaServico;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Categoria criarCategoria(@RequestBody @Valid Categoria categoria) {
        return categoriaServico.criarCategoria(categoria);
    }

    @GetMapping
    public List<Categoria> listarCategorias() {
        return categoriaServico.listarCategorias();
    }

    @GetMapping("/{id}")
    public Categoria buscarCategoriaPorId(@PathVariable Long id) {
        return categoriaServico.buscarCategoriaPorId(id);
    }

    @PutMapping("/{id}")
    public Categoria atualizarCategoria(@PathVariable Long id, @RequestBody @Valid Categoria categoria) {
        return categoriaServico.atualizarCategoria(id, categoria);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removerCategoria(@PathVariable Long id) {
        categoriaServico.removerCategoria(id);
    }
}
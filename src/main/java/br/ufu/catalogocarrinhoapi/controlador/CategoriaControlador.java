package br.ufu.catalogocarrinhoapi.controlador;

import br.ufu.catalogocarrinhoapi.modelo.Categoria;
import br.ufu.catalogocarrinhoapi.servico.CategoriaServico;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;


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
    public Page<Categoria> listarCategorias(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nome") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        return categoriaServico.listarCategoriasPaginadas(page, size, sortBy, direction);
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
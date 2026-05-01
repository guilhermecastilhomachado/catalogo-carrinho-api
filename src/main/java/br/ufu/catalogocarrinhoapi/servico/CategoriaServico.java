package br.ufu.catalogocarrinhoapi.servico;

import br.ufu.catalogocarrinhoapi.excecao.RecursoNaoEncontradoExcecao;
import br.ufu.catalogocarrinhoapi.modelo.Categoria;
import br.ufu.catalogocarrinhoapi.repositorio.CategoriaRepositorio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaServico {

    private final CategoriaRepositorio categoriaRepositorio;

    public Categoria criarCategoria(Categoria categoria) {
        return categoriaRepositorio.save(categoria);
    }

    public List<Categoria> listarCategorias() {
        return categoriaRepositorio.findAll();
    }

    public Categoria buscarCategoriaPorId(Long id) {
        return categoriaRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoExcecao("Categoria nao encontrada com id: " + id));
    }

    public Categoria atualizarCategoria(Long id, Categoria categoriaAtualizada) {
        Categoria categoriaExistente = buscarCategoriaPorId(id);

        categoriaExistente.setNome(categoriaAtualizada.getNome());
        categoriaExistente.setDescricao(categoriaAtualizada.getDescricao());

        return categoriaRepositorio.save(categoriaExistente);
    }

    public void removerCategoria(Long id) {
        Categoria categoria = buscarCategoriaPorId(id);
        categoriaRepositorio.delete(categoria);
    }
}
package br.ufu.catalogocarrinhoapi.repositorio;

import br.ufu.catalogocarrinhoapi.modelo.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepositorio extends JpaRepository<Categoria, Long> {
}
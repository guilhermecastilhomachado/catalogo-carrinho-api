package br.ufu.catalogocarrinhoapi.repositorio;

import br.ufu.catalogocarrinhoapi.modelo.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepositorio extends JpaRepository<Produto, Long> {
    Page<Produto> findByCategoriaId(Long categoriaId, Pageable pageable);
}
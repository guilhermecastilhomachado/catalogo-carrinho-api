package br.ufu.catalogocarrinhoapi.repositorio;

import br.ufu.catalogocarrinhoapi.modelo.Pedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PedidoRepositorio extends JpaRepository<Pedido, Long> {
    Optional<Pedido> findByCarrinhoIdOrigem(Long carrinhoIdOrigem);
    Page<Pedido> findAll(Pageable pageable);
}
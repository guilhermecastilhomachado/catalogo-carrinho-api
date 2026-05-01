package br.ufu.catalogocarrinhoapi.repositorio;

import br.ufu.catalogocarrinhoapi.modelo.ItemCarrinho;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemCarrinhoRepositorio extends JpaRepository<ItemCarrinho, Long> {
}
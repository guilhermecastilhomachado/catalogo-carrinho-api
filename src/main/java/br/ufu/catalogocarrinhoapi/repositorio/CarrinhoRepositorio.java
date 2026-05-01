package br.ufu.catalogocarrinhoapi.repositorio;

import br.ufu.catalogocarrinhoapi.modelo.Carrinho;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarrinhoRepositorio extends JpaRepository<Carrinho, Long> {
}
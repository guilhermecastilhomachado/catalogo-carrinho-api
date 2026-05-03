package br.ufu.catalogocarrinhoapi.servico;

import br.ufu.catalogocarrinhoapi.excecao.RecursoNaoEncontradoExcecao;
import br.ufu.catalogocarrinhoapi.modelo.Pedido;
import br.ufu.catalogocarrinhoapi.repositorio.PedidoRepositorio;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PedidoServico {

    private final PedidoRepositorio pedidoRepositorio;

    public Page<Pedido> listarPedidosPaginados(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        return pedidoRepositorio.findAll(pageable);
    }

    public Pedido buscarPedidoPorId(Long id) {
        return pedidoRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoExcecao("Pedido nao encontrado com id: " + id));
    }

    public Pedido buscarPedidoPorCarrinhoId(Long carrinhoId) {
        return pedidoRepositorio.findByCarrinhoIdOrigem(carrinhoId)
                .orElseThrow(() -> new RecursoNaoEncontradoExcecao("Pedido nao encontrado para o carrinho id: " + carrinhoId));
    }
}
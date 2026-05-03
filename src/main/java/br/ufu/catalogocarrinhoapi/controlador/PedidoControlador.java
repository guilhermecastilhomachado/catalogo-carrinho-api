package br.ufu.catalogocarrinhoapi.controlador;

import br.ufu.catalogocarrinhoapi.modelo.Pedido;
import br.ufu.catalogocarrinhoapi.servico.PedidoServico;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
public class PedidoControlador {

    private final PedidoServico pedidoServico;

    @GetMapping
    public Page<Pedido> listarPedidos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dataFinalizacao") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        return pedidoServico.listarPedidosPaginados(page, size, sortBy, direction);
    }

    @GetMapping("/{id}")
    public Pedido buscarPedidoPorId(@PathVariable Long id) {
        return pedidoServico.buscarPedidoPorId(id);
    }

    @GetMapping("/carrinho/{carrinhoId}")
    public Pedido buscarPedidoPorCarrinhoId(@PathVariable Long carrinhoId) {
        return pedidoServico.buscarPedidoPorCarrinhoId(carrinhoId);
    }
}
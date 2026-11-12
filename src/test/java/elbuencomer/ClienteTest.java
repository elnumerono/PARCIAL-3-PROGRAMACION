package elbuencomer;

import elbuencomer.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;

public class ClienteTest {
    private Cliente cliente;
    private Producto plato;

    @BeforeEach
    void setUp() {
        cliente = new Cliente("1094", "Laura Gómez", "310", "laura@correo.com", "Cra 14");
        plato = new PlatoFuerte("P01", "Bandeja", "Típica", 30000.0, 20, 500, TipoPreparacion.FRITO, true);
    }

    private Pedido pedido(String codigo) {
        Pedido p = new Pedido(codigo, LocalDate.now(), cliente, ModalidadEntrega.CONSUMO_RESTAURANTE);
        p.agregarProducto(plato, 1);
        return p;
    }

    @Test
    void agregaPedidoAlHistorial() {
        cliente.agregarPedido(pedido("PED1"));
        assertEquals(1, cliente.getListClientePedidos().size());
    }

    @Test
    void rechazaPedidoConCodigoDuplicado() {
        cliente.agregarPedido(pedido("PED1"));
        assertThrows(IllegalArgumentException.class, () -> cliente.agregarPedido(pedido("PED1")));
    }

    @Test
    void buscaPedidoPorCodigo() {
        Pedido p = pedido("PED1");
        cliente.agregarPedido(p);
        assertSame(p, cliente.buscarPedido("PED1"));
        assertNull(cliente.buscarPedido("NOEXISTE"));
    }

    @Test
    void calculaTotalComprasSinCancelados() {
        Pedido p1 = pedido("PED1");
        Pedido p2 = pedido("PED2");
        p2.cancelar();
        cliente.agregarPedido(p1);
        cliente.agregarPedido(p2);
        assertEquals(36000, cliente.calcularTotalCompras(), 0.001);
    }

    @Test
    void rechazaPedidoNulo() {
        assertThrows(IllegalArgumentException.class, () -> cliente.agregarPedido(null));
    }
}

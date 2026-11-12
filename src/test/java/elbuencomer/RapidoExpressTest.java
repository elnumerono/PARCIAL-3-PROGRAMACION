package elbuencomer;

import elbuencomer.model.Cliente;
import elbuencomer.model.ModalidadEntrega;
import elbuencomer.model.Pedido;
import elbuencomer.model.RapidoExpress;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;

public class RapidoExpressTest {
    private final RapidoExpress rapido = new RapidoExpress();

    @Test
    void calculaCostoDomicilio() {
        assertEquals(20000, rapido.calcularCostoDomicilio(10), 0.001);
    }

    @Test
    void aceptaRecorridoHasta12Km() {
        assertTrue(rapido.aceptaRecorrido(12));
    }

    @Test
    void rechazaRecorridoMayorA12Km() {
        assertFalse(rapido.aceptaRecorrido(12.5));
        assertThrows(IllegalArgumentException.class, () -> rapido.calcularCostoDomicilio(15));
    }

    @Test
    void solicitarServicioAsignaCostoYEmpresaAlPedido() {
        Cliente c = new Cliente("1", "Ana", "300", "a@a.com", "Cra 1");
        Pedido p = new Pedido("PED1", LocalDate.now(), c, ModalidadEntrega.DOMICILIO);
        assertTrue(rapido.solicitarServicio(p, 4));
        assertSame(rapido, p.getTheServicioPedido());
        assertEquals(11000, p.getCostoDomicilio(), 0.001);
    }

    @Test
    void solicitarServicioRechazaPedidoEnRestaurante() {
        Cliente c = new Cliente("1", "Ana", "300", "a@a.com", "Cra 1");
        Pedido p = new Pedido("PED1", LocalDate.now(), c, ModalidadEntrega.CONSUMO_RESTAURANTE);
        assertThrows(IllegalArgumentException.class, () -> rapido.solicitarServicio(p, 4));
    }
}

package elbuencomer;

public class EntregaSeguraTest {
    private final EntregaSegura segura = new EntregaSegura();

    @Test
    void calculaCostoSinRecargoHasta5Km() {
        assertEquals(12000, segura.calcularCostoDomicilio(5), 0.001);
    }

    @Test
    void calculaCostoConRecargoSiSupera5Km() {
        assertEquals(18000, segura.calcularCostoDomicilio(8), 0.001);
    }

    @Test
    void aceptaRecorridoHasta20Km() {
        assertTrue(segura.aceptaRecorrido(20));
    }

    @Test
    void rechazaRecorridoMayorA20KmOCero() {
        assertFalse(segura.aceptaRecorrido(21));
        assertFalse(segura.aceptaRecorrido(0));
    }

    @Test
    void solicitarServicioNoAceptadoNoModificaPedido() {
        Cliente c = new Cliente("1", "Ana", "300", "a@a.com", "Cra 1");
        Pedido p = new Pedido("PED1", LocalDate.now(), c, ModalidadEntrega.DOMICILIO);
        assertFalse(segura.solicitarServicio(p, 25));
        assertNull(p.getTheServicioPedido());
        assertEquals(0, p.getCostoDomicilio(), 0.001);
    }
}

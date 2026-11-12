package elbuencomer;

public class PedidoTest {
    private Cliente cliente;
    private Producto plato;
    private Producto bebida;

    @BeforeEach
    void setUp() {
        cliente = new Cliente("1094", "Laura", "310", "l@c.com", "Cra 14");
        plato = new PlatoFuerte("P01", "Bandeja", "TÃpica", 30000.0, 20, 500, TipoPreparacion.FRITO, true);
        bebida = new Bebida("B01", "Limonada", "Natural", 8000.0, 5, 400, 5, false, TemperaturaBebida.FRIO);
    }

    @Test
    void calculaValorProductosConVariasCantidades() {
        Pedido p = new Pedido("PED1", LocalDate.now(), cliente, ModalidadEntrega.CONSUMO_RESTAURANTE);
        p.agregarProducto(plato, 2);   // 72.000
        p.agregarProducto(bebida, 3);  // 26.400
        assertEquals(98400, p.calcularValorProductos(), 0.001);
    }

    @Test
    void valorTotalIncluyeDomicilio() {
        Pedido p = new Pedido("PED1", LocalDate.now(), cliente, ModalidadEntrega.DOMICILIO);
        p.agregarProducto(plato, 1);
        new RapidoExpress().solicitarServicio(p, 2); // 8.000
        assertEquals(44000, p.calcularValorTotal(), 0.001);
    }

    @Test
    void sumaCantidadSiSeAgregaElMismoProducto() {
        Pedido p = new Pedido("PED1", LocalDate.now(), cliente, ModalidadEntrega.CONSUMO_RESTAURANTE);
        p.agregarProducto(plato, 1);
        p.agregarProducto(plato, 2);
        assertEquals(1, p.getListPedidoProductos().size());
        assertEquals(1, p.getListPedidoDetalles().size());
        assertSame(plato, p.getListPedidoProductos().get(0));
        assertEquals(3, p.getListPedidoDetalles().get(0).cantidad());
        assertEquals(3, p.obtenerCantidad(plato));
    }

    @Test
    void pedidoCanceladoNoPuedeEntregarse() {
        Pedido p = new Pedido("PED1", LocalDate.now(), cliente, ModalidadEntrega.CONSUMO_RESTAURANTE);
        p.cancelar();
        assertEquals(EstadoPedido.CANCELADO, p.getEstado());
        assertThrows(IllegalStateException.class, p::marcarEntregado);
        assertFalse(p.generaIngresos());
    }

    @Test
    void confirmarEnlazaProductosYNoPermiteConfirmarDosVeces() {
        Pedido p = new Pedido("PED1", LocalDate.now(), cliente, ModalidadEntrega.CONSUMO_RESTAURANTE);
        p.agregarProducto(plato, 1);
        p.confirmar();
        assertEquals(EstadoPedido.CONFIRMADO, p.getEstado());
        assertSame(p, plato.getTheProductoPedido());
        assertThrows(IllegalStateException.class, p::confirmar);
    }
}

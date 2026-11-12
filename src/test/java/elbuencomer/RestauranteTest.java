package elbuencomer;

public class RestauranteTest {
    class RestauranteTest {

        private Restaurante restaurante;
        private Cliente cliente;
        private Producto plato;
        private Producto bebida;
        private RapidoExpress rapido;

        @BeforeEach
        void setUp() {
            restaurante = new Restaurante("El Buen Comer", "900", "Armenia");
            cliente = new Cliente("1094", "Laura", "310", "l@c.com", "Cra 14");
            plato = new PlatoFuerte("P01", "Bandeja", "TÃpica", 30000.0, 20, 500, TipoPreparacion.FRITO, true);
            bebida = new Bebida("B01", "Limonada", "Natural", 8000.0, 5, 400, 5, false, TemperaturaBebida.FRIO);
            rapido = new RapidoExpress();
            restaurante.registrarProducto(plato);
            restaurante.registrarProducto(bebida);
            restaurante.registrarCliente(cliente);
            restaurante.registrarServicioDomicilio(rapido);
        }

        @Test
        void rechazaProductoYClienteDuplicados() {
            Producto otro = new Postre("P01", "Otro", "x", 5000.0, 10, 200, 20, false);
            Cliente mismoDoc = new Cliente("1094", "Otra", "1", "o@c.com", "x");
            assertThrows(IllegalArgumentException.class, () -> restaurante.registrarProducto(otro));
            assertThrows(IllegalArgumentException.class, () -> restaurante.registrarCliente(mismoDoc));
        }

        @Test
        void confirmaPedidoYLoRegistraEnHistoriales() {
            Pedido p = new Pedido("PED1", LocalDate.now(), cliente, ModalidadEntrega.DOMICILIO);
            p.agregarProducto(plato, 1);
            assertTrue(restaurante.solicitarServicioDomicilio(p, rapido, 4));
            restaurante.confirmarPedido(p);
            assertEquals(EstadoPedido.CONFIRMADO, p.getEstado());
            assertSame(p, restaurante.buscarPedido("PED1"));
            assertTrue(cliente.getListClientePedidos().contains(p));
            assertTrue(rapido.getListServicioPedidos().contains(p));
            assertSame(p, plato.getTheProductoPedido());
        }

        @Test
        void noConfirmaPedidoConProductoQueNoCumpleCalidad() {
            Producto malo = new PlatoFuerte("P02", "Costilla", "x", 30000.0, 20, 1000, TipoPreparacion.ASADO, true);
            restaurante.registrarProducto(malo);
            Pedido p = new Pedido("PED1", LocalDate.now(), cliente, ModalidadEntrega.CONSUMO_RESTAURANTE);
            p.agregarProducto(malo, 1);
            assertThrows(IllegalArgumentException.class, () -> restaurante.confirmarPedido(p));
            assertNull(restaurante.buscarPedido("PED1"));
        }

        @Test
        void noConfirmaPedidoDeClienteNoRegistradoNiDomicilioSinEmpresa() {
            Cliente noRegistrado = new Cliente("999", "X", "1", "x@c.com", "x");
            Pedido p1 = new Pedido("PED1", LocalDate.now(), noRegistrado, ModalidadEntrega.CONSUMO_RESTAURANTE);
            p1.agregarProducto(plato, 1);
            assertThrows(IllegalArgumentException.class, () -> restaurante.confirmarPedido(p1));

            Pedido p2 = new Pedido("PED2", LocalDate.now(), cliente, ModalidadEntrega.DOMICILIO);
            p2.agregarProducto(plato, 1);
            assertFalse(restaurante.solicitarServicioDomicilio(p2, rapido, 15)); // supera 12 km
            assertThrows(IllegalArgumentException.class, () -> restaurante.confirmarPedido(p2));
        }

        @Test
        void calculaIngresosSeparandoDomiciliosYExcluyendoCancelados() {
            Pedido p1 = new Pedido("PED1", LocalDate.now(), cliente, ModalidadEntrega.DOMICILIO);
            p1.agregarProducto(plato, 2);   // 72.000
            restaurante.solicitarServicioDomicilio(p1, rapido, 2); // 8.000
            restaurante.confirmarPedido(p1);

            Pedido p2 = new Pedido("PED2", LocalDate.now(), cliente, ModalidadEntrega.CONSUMO_RESTAURANTE);
            p2.agregarProducto(bebida, 5);  // 44.000
            restaurante.confirmarPedido(p2);

            Pedido p3 = new Pedido("PED3", LocalDate.now(), cliente, ModalidadEntrega.CONSUMO_RESTAURANTE);
            p3.agregarProducto(plato, 10);
            restaurante.confirmarPedido(p3);
            restaurante.cancelarPedido("PED3");

            assertEquals(116000, restaurante.calcularIngresosVentas(), 0.001);
            assertEquals(8000, restaurante.calcularIngresosDomicilios(), 0.001);
            assertSame(bebida, restaurante.obtenerProductoMasVendido());
            assertEquals("Platos fuertes", restaurante.obtenerCategoriaMayoresIngresos());
        }
}

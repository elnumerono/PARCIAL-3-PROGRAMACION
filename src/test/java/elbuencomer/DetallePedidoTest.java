package elbuencomer;

public class DetallePedidoTest {
    private final Producto plato = new PlatoFuerte("P01", "Bandeja", "TÃpica", 30000.0, 20, 500, TipoPreparacion.FRITO, true);
    private final Producto bebida = new Bebida("B01", "Limonada", "Natural", 8000.0, 5, 400, 5, true, TemperaturaBebida.FRIO);

    @Test
    void calculaSubtotalDePlato() {
        assertEquals(72000, new DetallePedido(2).calcularSubtotal(plato), 0.001);
    }

    @Test
    void calculaSubtotalDeBebidaConRecargo() {
        assertEquals(35400, new DetallePedido(3).calcularSubtotal(bebida), 0.001);
    }

    @Test
    void rechazaCantidadCeroONegativa() {
        assertThrows(IllegalArgumentException.class, () -> new DetallePedido(0));
        assertThrows(IllegalArgumentException.class, () -> new DetallePedido(-1));
    }

    @Test
    void rechazaCalcularSubtotalConProductoNulo() {
        DetallePedido detalle = new DetallePedido(1);
        assertThrows(IllegalArgumentException.class, () -> detalle.calcularSubtotal(null));
    }

    @Test
    void sumarCantidadCreaNuevoDetalleSinModificarElOriginal() {
        DetallePedido original = new DetallePedido(2);
        DetallePedido sumado = original.sumarCantidad(3);
        assertEquals(5, sumado.cantidad());
        assertEquals(2, original.cantidad());
    }
}

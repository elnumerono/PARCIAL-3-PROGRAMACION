package elbuencomer;

public class BebidaTest {
    private Bebida crear(double volumen, double temperatura, boolean especial, TemperaturaBebida tipo) {
        return new Bebida("B01", "Limonada", "Natural", 8000.0, 5, volumen, temperatura, especial, tipo);
    }

    @Test
    void calculaPrecioVentaConIncrementoDel10PorcientoYRecargoEspecial() {
        assertEquals(8800, crear(400, 5, false, TemperaturaBebida.FRIO).calcularPrecioVenta(), 0.001);
        assertEquals(11800, crear(400, 5, true, TemperaturaBebida.FRIO).calcularPrecioVenta(), 0.001);
    }

    @Test
    void bebidaFriaCumpleCalidadEntre0y10Grados() {
        assertTrue(crear(400, 0, false, TemperaturaBebida.FRIO).cumpleCalidad());
        assertTrue(crear(400, 10, false, TemperaturaBebida.FRIO).cumpleCalidad());
        assertFalse(crear(400, 12, false, TemperaturaBebida.FRIO).cumpleCalidad());
    }

    @Test
    void bebidaCalienteCumpleCalidadEntre55y85Grados() {
        assertTrue(crear(250, 70, false, TemperaturaBebida.CALIENTE).cumpleCalidad());
        assertFalse(crear(250, 50, false, TemperaturaBebida.CALIENTE).cumpleCalidad());
        assertFalse(crear(250, 90, false, TemperaturaBebida.CALIENTE).cumpleCalidad());
    }

    @Test
    void noCumpleCalidadConVolumenFueraDeRango() {
        assertFalse(crear(100, 5, false, TemperaturaBebida.FRIO).cumpleCalidad());
        assertFalse(crear(1600, 5, false, TemperaturaBebida.FRIO).cumpleCalidad());
    }

    @Test
    void noCumpleCalidadSinTipoDeTemperatura() {
        assertFalse(crear(400, 5, false, null).cumpleCalidad());
    }
}

package elbuencomer;

public class PlatoFuerteTest {
    private PlatoFuerte crear(double peso, int tiempo, boolean coccion) {
        return new PlatoFuerte("P01", "Bandeja paisa", "TÃpica", 30000.0, tiempo, peso, TipoPreparacion.FRITO, coccion);
    }

    @Test
    void calculaPrecioVentaConIncrementoDel20Porciento() {
        assertEquals(36000, crear(500, 20, true).calcularPrecioVenta(), 0.001);
    }

    @Test
    void cumpleCalidadConValoresDentroDeRango() {
        assertTrue(crear(500, 30, true).cumpleCalidad());
    }

    @Test
    void noCumpleCalidadSiPesoFueraDeRango() {
        assertFalse(crear(950, 30, true).cumpleCalidad());
        assertFalse(crear(150, 30, true).cumpleCalidad());
    }

    @Test
    void noCumpleCalidadSiTiempoFueraDeRango() {
        assertFalse(crear(500, 5, true).cumpleCalidad());
        assertFalse(crear(500, 70, true).cumpleCalidad());
    }

    @Test
    void noCumpleCalidadSinCoccionAprobada() {
        assertFalse(crear(500, 30, false).cumpleCalidad());
    }
}

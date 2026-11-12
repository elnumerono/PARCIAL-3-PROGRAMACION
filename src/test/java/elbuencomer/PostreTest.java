package elbuencomer;

public class PostreTest {
    private Postre crear(double peso, double temp, boolean refrigeracion) {
        return new Postre("D01", "Natas", "Tradicional", 10000.0, 15, peso, temp, refrigeracion);
    }

    @Test
    void calculaPrecioVentaSinRefrigeracionCon15Porciento() {
        assertEquals(11500, crear(200, 20, false).calcularPrecioVenta(), 0.001);
    }

    @Test
    void calculaPrecioVentaRefrigeradoConIncrementoAdicional() {
        assertEquals(12000, crear(200, 4, true).calcularPrecioVenta(), 0.001);
    }

    @Test
    void cumpleCalidadRefrigeradoEntre0y6Grados() {
        assertTrue(crear(200, 4, true).cumpleCalidad());
        assertFalse(crear(200, 10, true).cumpleCalidad());
    }

    @Test
    void cumpleCalidadNoRefrigeradoEntre15y30Grados() {
        assertTrue(crear(200, 20, false).cumpleCalidad());
        assertFalse(crear(200, 35, false).cumpleCalidad());
    }

    @Test
    void noCumpleCalidadConPesoFueraDeRango() {
        assertFalse(crear(50, 4, true).cumpleCalidad());
        assertFalse(crear(600, 20, false).cumpleCalidad());
    }
}

package elbuencomer.model;

public class Bebida extends Producto {
    private double volumenMl;
    private double temperaturaServicio;
    private boolean preparacionEspecial;
    private TemperaturaBebida temperaturaBebida;

    public Bebida(String codigo, String nombre, String descripcion,
                  Double precioBase, int tiempoPreparacionMin,
                  double volumenMl, double temperaturaServicio,
                  boolean preparacionEspecial, TemperaturaBebida temperaturaBebida) {
        super(codigo, nombre, descripcion, precioBase, tiempoPreparacionMin);
        this.volumenMl = volumenMl;
        this.temperaturaServicio = temperaturaServicio;
        this.preparacionEspecial = preparacionEspecial;
        this.temperaturaBebida = temperaturaBebida;
    }
    /**
     * Las bebidas tienen un incremento del 10 % sobre el precio base y,
     * si requieren preparación especial, un recargo fijo de $3.000.
     */
    @Override
    public double calcularPrecioVenta() {
        final double incremento = 0.10;
        final double recargoPreparacionEspecial = 3000;
        double precio = getPrecioBase() + getPrecioBase() * incremento;
        if (preparacionEspecial) {
            precio += recargoPreparacionEspecial;
        }
        return precio;
    }

    /**
     * Volumen entre 150 y 1.500 ml y temperatura de servicio entre 0 °C y 10 °C
     * si la bebida es fría, o entre 55 °C y 85 °C si es caliente.
     */
    @Override
    public boolean cumpleCalidad() {
        boolean volumenValido = volumenMl >= 150 && volumenMl <= 1500;
        boolean temperaturaValida;
        if (temperaturaBebida == TemperaturaBebida.FRIO) {
            temperaturaValida = temperaturaServicio >= 0 && temperaturaServicio <= 10;
        } else if (temperaturaBebida == TemperaturaBebida.CALIENTE) {
            temperaturaValida = temperaturaServicio >= 55 && temperaturaServicio <= 85;
        } else {
            temperaturaValida = false; // sin definir si es fría o caliente
        }
        return volumenValido && temperaturaValida;
    }

    public double getTemperaturaServicio() {
        return temperaturaServicio;
    }

    public void setTemperaturaServicio(double temperaturaServicio) {
        this.temperaturaServicio = temperaturaServicio;
    }

    public double getVolumenMl() {
        return volumenMl;
    }

    public void setVolumenMl(double volumenMl) {
        this.volumenMl = volumenMl;
    }

    public boolean isPreparacionEspecial() {
        return preparacionEspecial;
    }

    public void setPreparacionEspecial(boolean preparacionEspecial) {
        this.preparacionEspecial = preparacionEspecial;
    }

    public TemperaturaBebida getTemperaturaBebida() {
        return temperaturaBebida;
    }

    public void setTemperaturaBebida(TemperaturaBebida temperaturaBebida) {
        this.temperaturaBebida = temperaturaBebida;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "Bebida{" +
                "volumenMl=" + volumenMl +
                ", temperaturaServicio=" + temperaturaServicio +
                ", preparacionEspecial=" + preparacionEspecial +
                ", temperaturaBebida=" + temperaturaBebida +
                '}';
    }
}

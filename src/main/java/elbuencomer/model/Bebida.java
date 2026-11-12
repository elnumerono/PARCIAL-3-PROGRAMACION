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
     * Metodo para calcular el precio de venta
     * @return precio
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
     * Verificacion de si cumple calidad
     * @return verificacion
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

    /**
     * Metodo toString
      * @return toString
     */
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

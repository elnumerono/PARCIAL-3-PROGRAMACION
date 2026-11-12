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

package elbuencomer.model;

public class PlatoFuerte extends Producto {

    private double pesoGramos;
    private TipoPreparacion tipoPreparacion;
    private boolean coccionAprobada;

    public PlatoFuerte(String codigo, String nombre, String descripcion, Double precioBase, int tiempoPreparacionMin, double pesoGramos, TipoPreparacion tipoPreparacion, boolean coccionAprobada) {
        super(codigo, nombre, descripcion, precioBase, tiempoPreparacionMin);
        this.pesoGramos = pesoGramos;
        this.tipoPreparacion = tipoPreparacion;
        this.coccionAprobada = coccionAprobada;
    }

    /**
     * calcular el precio de venta
     * @return precio
     */
    @Override
    public double calcularPrecioVenta() {
        final double incremento = 0.20;
        return getPrecioBase() + getPrecioBase() * incremento;
    }

    /**
     * verificar si cumple calidad
     * @return verificacion
     */
    @Override
    public boolean cumpleCalidad() {
        boolean pesoValido = pesoGramos >= 200 && pesoGramos <= 900;
        boolean tiempoValido = getTiempoPreparacionMin() >= 10 && getTiempoPreparacionMin() <= 60;
        return pesoValido && tiempoValido && coccionAprobada;
    }

    public double getPesoGramos() {
        return pesoGramos;
    }

    public void setPesoGramos(double pesoGramos) {
        this.pesoGramos = pesoGramos;
    }

    public TipoPreparacion getTipoPreparacion() {
        return tipoPreparacion;
    }

    public void setTipoPreparacion(TipoPreparacion tipoPreparacion) {
        this.tipoPreparacion = tipoPreparacion;
    }

    public boolean isCoccionAprobada() {
        return coccionAprobada;
    }

    public void setCoccionAprobada(boolean coccionAprobada) {
        this.coccionAprobada = coccionAprobada;
    }

    /**
     * metodo toString
     * @return toString
     */
    @Override
    public String toString() {
        return "PlatoFuerte{" +
                "pesoGramos=" + pesoGramos +
                ", tipoPreparacion=" + tipoPreparacion +
                ", coccionAprobada=" + coccionAprobada +
                '}';
    }
}

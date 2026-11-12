package elbuencomer.model;

public class Postre extends Producto {

    private double pesoGramos;
    private double temperaturaAlmacenamiento;
    private boolean requiereRefrigeracion;

    public Postre(String codigo, String nombre, String descripcion, Double precioBase, int tiempoPreparacionMin, double pesoGramos, double temperaturaAlmacenamiento, boolean requiereRefrigeracion) {
        super(codigo, nombre, descripcion, precioBase, tiempoPreparacionMin);
        this.pesoGramos = pesoGramos;
        this.temperaturaAlmacenamiento = temperaturaAlmacenamiento;
        this.requiereRefrigeracion = requiereRefrigeracion;
    }

    /**
     * Calcular precio de venta
     * @return precio
     */
    @Override
    public double calcularPrecioVenta() {
        final double incremento = 0.15;
        final double incrementoRefrigeracion = 0.05;
        double precio = getPrecioBase() + getPrecioBase() * incremento;
        if (requiereRefrigeracion) {
            precio += getPrecioBase() * incrementoRefrigeracion;
        }
        return precio;
    }

    /**
     * Verificacion de calidad
     * @return verificacion
     */
    @Override
    public boolean cumpleCalidad() {
        boolean pesoValido = pesoGramos >= 80 && pesoGramos <= 500;
        boolean temperaturaValida;
        if (requiereRefrigeracion) {
            temperaturaValida = temperaturaAlmacenamiento >= 0 && temperaturaAlmacenamiento <= 6;
        } else {
            temperaturaValida = temperaturaAlmacenamiento >= 15 && temperaturaAlmacenamiento <= 30;
        }
        return pesoValido && temperaturaValida;
    }

    public double getPesoGramos() {
        return pesoGramos;
    }

    public void setPesoGramos(double pesoGramos) {
        this.pesoGramos = pesoGramos;
    }

    public boolean isRequiereRefrigeracion() {
        return requiereRefrigeracion;
    }

    public void setRequiereRefrigeracion(boolean requiereRefrigeracion) {
        this.requiereRefrigeracion = requiereRefrigeracion;
    }

    public double getTemperaturaAlmacenamiento() {
        return temperaturaAlmacenamiento;
    }

    public void setTemperaturaAlmacenamiento(double temperaturaAlmacenamiento) {
        this.temperaturaAlmacenamiento = temperaturaAlmacenamiento;
    }

    /**
     * metodo toString
      * @return toString
     */
    @Override
    public String toString() {
        return "Postre{" +
                "pesoGramos=" + pesoGramos +
                ", temperaturaAlmacenamiento=" + temperaturaAlmacenamiento +
                ", requiereRefrigeracion=" + requiereRefrigeracion +
                '}';
    }
}

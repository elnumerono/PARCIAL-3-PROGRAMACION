package elbuencomer.model;

public abstract class Producto {
    private String codigo;
    private String nombre;
    private String descripcion;
    private Double precioBase;
    private int tiempoPreparacionMin;

    private Pedido theProductoPedido;

    public Producto(String codigo, String nombre, String descripcion, Double precioBase, int tiempoPreparacionMin) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precioBase = precioBase;
        this.tiempoPreparacionMin = tiempoPreparacionMin;
        this.theProductoPedido = null;
    }
    /**
     * Calcula el precio de venta del producto según su categoría
     * y sus características particulares.
     */
    public abstract double calcularPrecioVenta();

    /**
     * Verifica si el producto cumple las condiciones de calidad
     * exigidas para incluirse en un pedido confirmado.
     */
    public abstract boolean cumpleCalidad();

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getPrecioBase() {
        return precioBase;
    }

    public void setPrecioBase(Double precioBase) {
        this.precioBase = precioBase;
    }

    public int getTiempoPreparacionMin() {
        return tiempoPreparacionMin;
    }

    public void setTiempoPreparacionMin(int tiempoPreparacionMin) {
        this.tiempoPreparacionMin = tiempoPreparacionMin;
    }

    public Pedido getTheProductoPedido() {
        return theProductoPedido;
    }

    public void setTheProductoPedido(Pedido theProductoPedido) {
        this.theProductoPedido = theProductoPedido;
    }

    /**
     * Metodo toString
      * @return toString
     */
    @Override
    public String toString() {
        return "Producto{" +
                "codigo='" + codigo + '\'' +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", precioBase=" + precioBase +
                ", tiempoPreparacionMin=" + tiempoPreparacionMin +
                '}';
    }
}

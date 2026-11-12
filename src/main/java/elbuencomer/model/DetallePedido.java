package elbuencomer.model;

public record DetallePedido(int cantidad) {
    public DetallePedido {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
        }
    }

    /**
     * Subtotal del detalle: precio de venta del producto por la cantidad.
     */
    public double calcularSubtotal(Producto producto) {
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo.");
        }
        return producto.calcularPrecioVenta() * cantidad;
    }

    /**
     * Como el record es inmutable, sumar unidades devuelve un nuevo detalle.
     */
    public DetallePedido sumarCantidad(int cantidadAdicional) {
        return new DetallePedido(cantidad + cantidadAdicional);
    }

    // El record genera automáticamente el getter cantidad().
    // Al ser inmutable no tiene setters: para cambiar la cantidad se crea un nuevo detalle.
}

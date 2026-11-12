package elbuencomer.model;

public record DetallePedido(int cantidad) {
    /**
     * Metodo de detalle de pedido
     * @param cantidad de pedido
     */
    public DetallePedido {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
        }
    }

    /**
     * Metodo para calcular subtotal
     * @param producto seleccionado
     * @return subtotal
     */
    public double calcularSubtotal(Producto producto) {
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo.");
        }
        return producto.calcularPrecioVenta() * cantidad;
    }

    /**
     * Detalle pedido nuevo
     * @param cantidadAdicional a adicionar
     * @return Nuevo detalle pedido
     */
    public DetallePedido sumarCantidad(int cantidadAdicional) {
        return new DetallePedido(cantidad + cantidadAdicional);
    }

    // El record genera automáticamente el getter cantidad().
    // Al ser inmutable no tiene setters: para cambiar la cantidad se crea un nuevo detalle.
}

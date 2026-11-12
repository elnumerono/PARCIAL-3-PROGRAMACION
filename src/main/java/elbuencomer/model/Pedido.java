package elbuencomer.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private String codigo;
    private LocalDate fecha;
    private EstadoPedido estado;
    private ModalidadEntrega modalidad;
    private double distanciaKm;
    private double costoDomicilio;

    private Cliente theClientePedido;
    private ServicioDomicilio theServicioPedido;
    private List<Producto> listPedidoProductos;
    private List<DetallePedido> listPedidoDetalles;
    // listPedidoProductos.get(i) corresponde a listPedidoDetalles.get(i) (producto y su cantidad)

    public Pedido(String codigo, LocalDate fecha, Cliente theClientePedido, ModalidadEntrega modalidad) {
        this.codigo = codigo;
        this.fecha = fecha;
        this.theClientePedido = theClientePedido;
        this.modalidad = modalidad;
        this.estado = EstadoPedido.PENDIENTE;
        this.distanciaKm = 0;
        this.costoDomicilio = 0;
        this.theServicioPedido = null;
        this.listPedidoProductos = new ArrayList<>();
        this.listPedidoDetalles = new ArrayList<>();
    }

    /**
     * Metodo de agregar producto a pedido
     * @param producto seleccionado
     * @param cantidad que desea añadir
     */
    public void agregarProducto(Producto producto, int cantidad) {
        if (estado != EstadoPedido.PENDIENTE) {
            throw new IllegalStateException("Solo se pueden agregar productos a un pedido pendiente.");
        }
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo.");
        }
        DetallePedido nuevo = new DetallePedido(cantidad); // valida cantidad > 0
        int indice = buscarIndiceProducto(producto.getCodigo());
        if (indice >= 0) {
            listPedidoDetalles.set(indice, listPedidoDetalles.get(indice).sumarCantidad(cantidad));
        } else {
            listPedidoProductos.add(producto);
            listPedidoDetalles.add(nuevo);
        }
    }

    /**
     * Buscar producto
     * @param codigoProducto del producto
     * @return
     */
    public int buscarIndiceProducto(String codigoProducto) {
        for (int i = 0; i < listPedidoProductos.size(); i++) {
            if (listPedidoProductos.get(i).getCodigo().equalsIgnoreCase(codigoProducto)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Cantidad solicitada de un producto
     * @param producto a verificar
     * @return cantidad
     */
    public int obtenerCantidad(Producto producto) {
        int indice = buscarIndiceProducto(producto.getCodigo());
        return indice >= 0 ? listPedidoDetalles.get(indice).cantidad() : 0;
    }

    /**
     * Subtotal de pedido
     * @param indice de pedido
     * @return subtotal
     */
    public double calcularSubtotal(int indice) {
        return listPedidoDetalles.get(indice).calcularSubtotal(listPedidoProductos.get(indice));
    }

    /**
     * Calcular el valor de los prodcutos
     * @return total
     */
    public double calcularValorProductos() {
        double total = 0;
        for (int i = 0; i < listPedidoProductos.size(); i++) {
            total += calcularSubtotal(i);
        }
        return total;
    }

    /**
     * Valor total del pedido
     * @return total
     */
    public double calcularValorTotal() {
        double total = calcularValorProductos();
        if (esDomicilio()) {
            total += costoDomicilio;
        }
        return total;
    }

    /**
     * Verificar calidad de productos
     * @return verificacion
     */
    public boolean productosCumplenCalidad() {
        for (Producto producto : listPedidoProductos) {
            if (!producto.cumpleCalidad()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Definir modalidad del domicilio
      * @return modalidad
     */
    public boolean esDomicilio() {
        return modalidad == ModalidadEntrega.DOMICILIO;
    }

    /**
     * Definir ingresos
     * @return estado
     */
    public boolean generaIngresos() {
        return estado == EstadoPedido.CONFIRMADO || estado == EstadoPedido.ENTREGADO;
    }

    /**
     * Cambia el estado a CONFIRMADO y enlaza cada producto con este pedido
     * (relación theProductoPedido). Evita confirmaciones repetidas.
     * Las demás verificaciones las realiza el Restaurante antes de llamar este método.
     */
    public void confirmar() {
        if (estado != EstadoPedido.PENDIENTE) {
            throw new IllegalStateException("El pedido " + codigo + " no está pendiente (estado: " + estado + ").");
        }
        estado = EstadoPedido.CONFIRMADO;
        for (Producto producto : listPedidoProductos) {
            producto.setTheProductoPedido(this);
        }
    }

    /**
     * Un pedido confirmado puede marcarse como entregado.
     */
    public void marcarEntregado() {
        if (estado != EstadoPedido.CONFIRMADO) {
            throw new IllegalStateException("Solo un pedido confirmado puede entregarse (estado: " + estado + ").");
        }
        estado = EstadoPedido.ENTREGADO;
    }

    /**
     * Cancela el pedido. Un pedido entregado o ya cancelado no puede cancelarse.
     */
    public void cancelar() {
        if (estado == EstadoPedido.ENTREGADO || estado == EstadoPedido.CANCELADO) {
            throw new IllegalStateException("El pedido no puede cancelarse (estado: " + estado + ").");
        }
        estado = EstadoPedido.CANCELADO;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    public ModalidadEntrega getModalidad() {
        return modalidad;
    }

    public void setModalidad(ModalidadEntrega modalidad) {
        this.modalidad = modalidad;
    }

    public double getDistanciaKm() {
        return distanciaKm;
    }

    public void setDistanciaKm(double distanciaKm) {
        this.distanciaKm = distanciaKm;
    }

    public double getCostoDomicilio() {
        return costoDomicilio;
    }

    public void setCostoDomicilio(double costoDomicilio) {
        this.costoDomicilio = costoDomicilio;
    }

    public Cliente getTheClientePedido() {
        return theClientePedido;
    }

    public void setTheClientePedido(Cliente theClientePedido) {
        this.theClientePedido = theClientePedido;
    }

    public ServicioDomicilio getTheServicioPedido() {
        return theServicioPedido;
    }

    public void setTheServicioPedido(ServicioDomicilio theServicioPedido) {
        this.theServicioPedido = theServicioPedido;
    }

    public List<Producto> getListPedidoProductos() {
        return listPedidoProductos;
    }

    public void setListPedidoProductos(List<Producto> listPedidoProductos) {
        this.listPedidoProductos = listPedidoProductos;
    }

    public List<DetallePedido> getListPedidoDetalles() {
        return listPedidoDetalles;
    }

    public void setListPedidoDetalles(List<DetallePedido> listPedidoDetalles) {
        this.listPedidoDetalles = listPedidoDetalles;
    }

    /**
     * toString
     * @return toString
     */
    @java.lang.Override
    public java.lang.String toString() {
        return "Pedido{" +
                "codigo='" + codigo + '\'' +
                ", fecha=" + fecha +
                ", estado=" + estado +
                ", modalidad=" + modalidad +
                ", distanciaKm=" + distanciaKm +
                ", costoDomicilio=" + costoDomicilio +
                '}';
    }
}

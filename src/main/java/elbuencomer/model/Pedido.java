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

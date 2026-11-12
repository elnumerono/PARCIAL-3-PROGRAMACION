package elbuencomer.model;

import java.util.List;

public interface ServicioDomicilio {
    /**
     * Indica si la empresa acepta un recorrido de la distancia dada.
     */
    boolean aceptaRecorrido(double distanciaKm);

    /**
     * Calcula el costo del domicilio para la distancia dada.
     */
    double calcularCostoDomicilio(double distanciaKm);

    /**
     * Pedidos atendidos por la empresa (relación listServicioPedidos).
     * Una interfaz no puede tener atributos, por eso cada empresa guarda su lista.
     */
    List<Pedido> getListServicioPedidos();

    /**
     * Procedimiento común para solicitar el servicio: si la empresa acepta el
     * recorrido, asigna al pedido la empresa, la distancia y el costo del domicilio.
     *
     * @return true si la empresa aceptó el servicio, false en caso contrario.
     */
    default boolean solicitarServicio(Pedido pedido, double distanciaKm) {
        if (pedido == null) {
            throw new IllegalArgumentException("El pedido no puede ser nulo.");
        }
        if (pedido.getModalidad() != ModalidadEntrega.DOMICILIO) {
            throw new IllegalArgumentException("Solo los pedidos a domicilio requieren empresa de entrega.");
        }
        if (!aceptaRecorrido(distanciaKm)) {
            return false;
        }
        pedido.setTheServicioPedido(this);
        pedido.setDistanciaKm(distanciaKm);
        pedido.setCostoDomicilio(calcularCostoDomicilio(distanciaKm));
        return true;
    }
}

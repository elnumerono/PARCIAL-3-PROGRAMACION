package elbuencomer.model;

import java.util.ArrayList;
import java.util.List;

public class RapidoExpress implements ServicioDomicilio {

    public static final double TARIFA_BASE = 5000;
    public static final double VALOR_KILOMETRO = 1500;
    public static final double DISTANCIA_MAXIMA = 12;

    private List<Pedido> listServicioPedidos;

    public RapidoExpress() {
        this.listServicioPedidos = new ArrayList<>();
    }

    @Override
    public boolean aceptaRecorrido(double distanciaKm) {
        return distanciaKm > 0 && distanciaKm <= DISTANCIA_MAXIMA;
    }

    @Override
    public double calcularCostoDomicilio(double distanciaKm) {
        if (!aceptaRecorrido(distanciaKm)) {
            throw new IllegalArgumentException("RápidoExpress no acepta recorridos de " + distanciaKm + " km.");
        }
        return TARIFA_BASE + VALOR_KILOMETRO * distanciaKm;
    }

    @Override
    public List<Pedido> getListServicioPedidos() {
        return listServicioPedidos;
    }

    public void setListServicioPedidos(List<Pedido> listServicioPedidos) {
        this.listServicioPedidos = listServicioPedidos;
    }

    @Override
    public String toString() {
        return "RapidoExpress{" +
                "listServicioPedidos=" + listServicioPedidos +
                '}';
    }
}

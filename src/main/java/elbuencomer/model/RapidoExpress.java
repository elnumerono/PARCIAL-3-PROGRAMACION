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

    /**
     * Verificar si acepta el recorrido
      * @param distanciaKm a recorrer
     * @return verificacion
     */
    @Override
    public boolean aceptaRecorrido(double distanciaKm) {
        return distanciaKm > 0 && distanciaKm <= DISTANCIA_MAXIMA;
    }

    /**
     * Calcular costo domicilio
      * @param distanciaKm a recorrer
     * @return costo
     */
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

    /**
     * Metodo toString
      * @return toString
     */
    @Override
    public String toString() {
        return "RapidoExpress{" +
                "listServicioPedidos=" + listServicioPedidos +
                '}';
    }
}

package elbuencomer.model;

import java.util.ArrayList;
import java.util.List;

/**
 * EntregaSegura: $7.000 de tarifa base + $1.000 por kilómetro,
 * con un recargo de $3.000 cuando la distancia supera 5 km.
 * Acepta recorridos de hasta 20 km.
 */
public class EntregaSegura implements ServicioDomicilio {

    public static final double TARIFA_BASE = 7000;
    public static final double VALOR_KILOMETRO = 1000;
    public static final double RECARGO_DISTANCIA = 3000;
    public static final double DISTANCIA_RECARGO = 5;
    public static final double DISTANCIA_MAXIMA = 20;

    private List<Pedido> listServicioPedidos;

    public EntregaSegura() {
        this.listServicioPedidos = new ArrayList<>();
    }

    /**
     * Metodo para ver si acepta el recorrido
      * @param distanciaKm a recorrer
     * @return verificacion
     */
    @Override
    public boolean aceptaRecorrido(double distanciaKm) {
        return distanciaKm > 0 && distanciaKm <= DISTANCIA_MAXIMA;
    }

    /**
     * Metodo para calcular el costo del domicilio
      * @param distanciaKm a recorrer
     * @return costo
     */
    @Override
    public double calcularCostoDomicilio(double distanciaKm) {
        if (!aceptaRecorrido(distanciaKm)) {
            throw new IllegalArgumentException("EntregaSegura no acepta recorridos de " + distanciaKm + " km.");
        }
        double costo = TARIFA_BASE + VALOR_KILOMETRO * distanciaKm;
        if (distanciaKm > DISTANCIA_RECARGO) {
            costo += RECARGO_DISTANCIA;
        }
        return costo;
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
        return "EntregaSegura{}";
    }
}

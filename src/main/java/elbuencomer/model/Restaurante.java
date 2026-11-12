package elbuencomer.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Restaurante {
    private String nombreComercial;
    private String nit;
    private String direccion;

    private List<Producto> listRestauranteProductos;
    private List<Cliente> listRestauranteClientes;
    private List<Pedido> listRestaurantePedidos;
    private List<ServicioDomicilio> listRestauranteDomicilios;

    public Restaurante(String nombreComercial, String nit, String direccion) {
        this.nombreComercial = nombreComercial;
        this.nit = nit;
        this.direccion = direccion;
        this.listRestauranteProductos = new ArrayList<>();
        this.listRestauranteClientes = new ArrayList<>();
        this.listRestaurantePedidos = new ArrayList<>();
        this.listRestauranteDomicilios = new ArrayList<>();
    }

    // =====================================================================
    // 1. Registrar productos
    // =====================================================================

    public void registrarProducto(Producto producto) {
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo.");
        }
        if (producto.getCodigo() == null || producto.getCodigo().isBlank()) {
            throw new IllegalArgumentException("El producto debe tener un código.");
        }
        if (buscarProducto(producto.getCodigo()) != null) {
            throw new IllegalArgumentException("Ya existe un producto con el código " + producto.getCodigo() + ".");
        }
        listRestauranteProductos.add(producto);
    }

    public Producto buscarProducto(String codigo) {
        for (Producto producto : listRestauranteProductos) {
            if (producto.getCodigo().equalsIgnoreCase(codigo)) {
                return producto;
            }
        }
        return null;
    }

    // =====================================================================
    // 2. Registrar clientes
    // =====================================================================

    public void registrarCliente(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("El cliente no puede ser nulo.");
        }
        if (cliente.getDocumento() == null || cliente.getDocumento().isBlank()) {
            throw new IllegalArgumentException("El cliente debe tener un documento.");
        }
        if (buscarCliente(cliente.getDocumento()) != null) {
            throw new IllegalArgumentException("Ya existe un cliente con el documento " + cliente.getDocumento() + ".");
        }
        listRestauranteClientes.add(cliente);
    }

    public Cliente buscarCliente(String documento) {
        for (Cliente cliente : listRestauranteClientes) {
            if (cliente.getDocumento().equals(documento)) {
                return cliente;
            }
        }
        return null;
    }

    // =====================================================================
    // Empresas de domicilio
    // =====================================================================

    public void registrarServicioDomicilio(ServicioDomicilio servicio) {
        if (servicio == null) {
            throw new IllegalArgumentException("La empresa de domicilio no puede ser nula.");
        }
        if (listRestauranteDomicilios.contains(servicio)) {
            throw new IllegalArgumentException("La empresa de domicilio ya está registrada.");
        }
        listRestauranteDomicilios.add(servicio);
    }

    /**
     * Solicita el servicio de domicilio a una de las empresas contratadas
     * mediante el procedimiento común de la interfaz ServicioDomicilio.
     *
     * @return true si la empresa aceptó el recorrido.
     */
    public boolean solicitarServicioDomicilio(Pedido pedido, ServicioDomicilio servicio, double distanciaKm) {
        if (servicio == null || !listRestauranteDomicilios.contains(servicio)) {
            throw new IllegalArgumentException("La empresa de domicilio no está contratada por el restaurante.");
        }
        return servicio.solicitarServicio(pedido, distanciaKm);
    }

    // =====================================================================
    // 3. Registrar (confirmar) pedidos
    // =====================================================================

    /**
     * Verifica todas las condiciones y confirma el pedido. Si todo es correcto,
     * lo registra en el historial del restaurante, del cliente y de la empresa de domicilio.
     *
     * @throws IllegalArgumentException si alguna condición no se cumple.
     * @throws IllegalStateException    si el pedido ya fue confirmado (confirmación repetida).
     */
    public void confirmarPedido(Pedido pedido) {
        if (pedido == null) {
            throw new IllegalArgumentException("El pedido no puede ser nulo.");
        }
        if (pedido.getEstado() != EstadoPedido.PENDIENTE) {
            throw new IllegalStateException("El pedido " + pedido.getCodigo() + " ya fue procesado (estado: " + pedido.getEstado() + ").");
        }
        if (pedido.getCodigo() == null || pedido.getCodigo().isBlank()) {
            throw new IllegalArgumentException("El pedido debe tener un código.");
        }
        if (buscarPedido(pedido.getCodigo()) != null) {
            throw new IllegalArgumentException("Ya existe un pedido con el código " + pedido.getCodigo() + ".");
        }

        Cliente cliente = pedido.getTheClientePedido();
        if (cliente == null || buscarCliente(cliente.getDocumento()) != cliente) {
            throw new IllegalArgumentException("El cliente del pedido no está registrado.");
        }

        List<Producto> productos = pedido.getListPedidoProductos();
        List<DetallePedido> detalles = pedido.getListPedidoDetalles();
        if (productos.isEmpty()) {
            throw new IllegalArgumentException("El pedido no tiene productos.");
        }
        if (productos.size() != detalles.size()) {
            throw new IllegalArgumentException("Cada producto del pedido debe tener su detalle con la cantidad.");
        }
        for (int i = 0; i < productos.size(); i++) {
            Producto producto = productos.get(i);
            DetallePedido detalle = detalles.get(i);
            if (!listRestauranteProductos.contains(producto)) {
                throw new IllegalArgumentException("El producto " + producto.getCodigo() + " no pertenece al menú.");
            }
            if (detalle.cantidad() <= 0) {
                throw new IllegalArgumentException("La cantidad del producto " + producto.getCodigo() + " debe ser mayor que cero.");
            }
            if (!producto.cumpleCalidad()) {
                throw new IllegalArgumentException("El producto " + producto.getCodigo() + " no cumple las condiciones de calidad.");
            }
        }

        ServicioDomicilio servicio = pedido.getTheServicioPedido();
        if (pedido.esDomicilio()) {
            if (servicio == null || !listRestauranteDomicilios.contains(servicio)
                    || !servicio.aceptaRecorrido(pedido.getDistanciaKm())) {
                throw new IllegalArgumentException("Ninguna empresa de domicilio ha aceptado el servicio.");
            }
        }

        pedido.confirmar();
        listRestaurantePedidos.add(pedido);
        cliente.agregarPedido(pedido);
        if (pedido.esDomicilio()) {
            servicio.getListServicioPedidos().add(pedido);
        }
    }

    public Pedido buscarPedido(String codigo) {
        for (Pedido pedido : listRestaurantePedidos) {
            if (pedido.getCodigo().equalsIgnoreCase(codigo)) {
                return pedido;
            }
        }
        return null;
    }

    // =====================================================================
    // 4. Calcular el valor total de un pedido
    // =====================================================================

    public double calcularValorTotalPedido(String codigo) {
        Pedido pedido = buscarPedido(codigo);
        if (pedido == null) {
            throw new IllegalArgumentException("No existe un pedido con el código " + codigo + ".");
        }
        return pedido.calcularValorTotal();
    }

    // =====================================================================
    // 5. Historial de pedidos de un cliente
    // =====================================================================

    public List<Pedido> consultarHistorialCliente(String documento) {
        Cliente cliente = buscarCliente(documento);
        if (cliente == null) {
            throw new IllegalArgumentException("No existe un cliente con el documento " + documento + ".");
        }
        return cliente.getListClientePedidos();
    }

    // =====================================================================
    // Estado de los pedidos
    // =====================================================================

    public void marcarPedidoEntregado(String codigo) {
        Pedido pedido = buscarPedido(codigo);
        if (pedido == null) {
            throw new IllegalArgumentException("No existe un pedido con el código " + codigo + ".");
        }
        pedido.marcarEntregado();
    }

    public void cancelarPedido(String codigo) {
        Pedido pedido = buscarPedido(codigo);
        if (pedido == null) {
            throw new IllegalArgumentException("No existe un pedido con el código " + codigo + ".");
        }
        pedido.cancelar();
    }

    // =====================================================================
    // Consultas y reportes
    // =====================================================================

    /**
     * Productos del menú que cumplen las condiciones de calidad (pueden venderse).
     */
    public List<Producto> consultarProductosDisponibles() {
        List<Producto> disponibles = new ArrayList<>();
        for (Producto producto : listRestauranteProductos) {
            if (producto.cumpleCalidad()) {
                disponibles.add(producto);
            }
        }
        return disponibles;
    }

    /**
     * Productos del menú que NO cumplen las condiciones de calidad.
     */
    public List<Producto> consultarProductosNoCumplenCalidad() {
        List<Producto> noCumplen = new ArrayList<>();
        for (Producto producto : listRestauranteProductos) {
            if (!producto.cumpleCalidad()) {
                noCumplen.add(producto);
            }
        }
        return noCumplen;
    }

    /**
     * Ingresos propios del restaurante: valor de los productos de los pedidos
     * confirmados o entregados (sin domicilios y sin pedidos cancelados).
     */
    public double calcularIngresosVentas() {
        double total = 0;
        for (Pedido pedido : listRestaurantePedidos) {
            if (pedido.generaIngresos()) {
                total += pedido.calcularValorProductos();
            }
        }
        return total;
    }

    /**
     * Valores correspondientes a domicilios, diferenciados de los ingresos del restaurante.
     */
    public double calcularIngresosDomicilios() {
        double total = 0;
        for (Pedido pedido : listRestaurantePedidos) {
            if (pedido.generaIngresos() && pedido.esDomicilio()) {
                total += pedido.getCostoDomicilio();
            }
        }
        return total;
    }

    /**
     * Producto con mayor cantidad de unidades vendidas (sin contar pedidos cancelados).
     *
     * @return el producto o null si no hay ventas.
     */
    public Producto obtenerProductoMasVendido() {
        Map<Producto, Integer> unidades = new LinkedHashMap<>();
        for (Pedido pedido : listRestaurantePedidos) {
            if (pedido.generaIngresos()) {
                for (int i = 0; i < pedido.getListPedidoProductos().size(); i++) {
                    unidades.merge(pedido.getListPedidoProductos().get(i),
                            pedido.getListPedidoDetalles().get(i).cantidad(), Integer::sum);
                }
            }
        }
        Producto masVendido = null;
        int maxUnidades = 0;
        for (Map.Entry<Producto, Integer> entrada : unidades.entrySet()) {
            if (entrada.getValue() > maxUnidades) {
                maxUnidades = entrada.getValue();
                masVendido = entrada.getKey();
            }
        }
        return masVendido;
    }

    /**
     * Categoría (Platos fuertes, Bebidas o Postres) que genera mayores ingresos.
     *
     * @return el nombre de la categoría o null si no hay ventas.
     */
    public String obtenerCategoriaMayoresIngresos() {
        double ingresosPlatos = 0;
        double ingresosBebidas = 0;
        double ingresosPostres = 0;
        for (Pedido pedido : listRestaurantePedidos) {
            if (pedido.generaIngresos()) {
                for (int i = 0; i < pedido.getListPedidoProductos().size(); i++) {
                    Producto producto = pedido.getListPedidoProductos().get(i);
                    double subtotal = pedido.calcularSubtotal(i);
                    if (producto instanceof PlatoFuerte) {
                        ingresosPlatos += subtotal;
                    } else if (producto instanceof Bebida) {
                        ingresosBebidas += subtotal;
                    } else if (producto instanceof Postre) {
                        ingresosPostres += subtotal;
                    }
                }
            }
        }
        if (ingresosPlatos == 0 && ingresosBebidas == 0 && ingresosPostres == 0) {
            return null;
        }
        if (ingresosPlatos >= ingresosBebidas && ingresosPlatos >= ingresosPostres) {
            return "Platos fuertes";
        } else if (ingresosBebidas >= ingresosPostres) {
            return "Bebidas";
        } else {
            return "Postres";
        }
    }

    public String getNombreComercial() {
        return nombreComercial;
    }

    public void setNombreComercial(String nombreComercial) {
        this.nombreComercial = nombreComercial;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public List<Producto> getListRestauranteProductos() {
        return listRestauranteProductos;
    }

    public void setListRestauranteProductos(List<Producto> listRestauranteProductos) {
        this.listRestauranteProductos = listRestauranteProductos;
    }

    public List<Cliente> getListRestauranteClientes() {
        return listRestauranteClientes;
    }

    public void setListRestauranteClientes(List<Cliente> listRestauranteClientes) {
        this.listRestauranteClientes = listRestauranteClientes;
    }

    public List<Pedido> getListRestaurantePedidos() {
        return listRestaurantePedidos;
    }

    public void setListRestaurantePedidos(List<Pedido> listRestaurantePedidos) {
        this.listRestaurantePedidos = listRestaurantePedidos;
    }

    public List<ServicioDomicilio> getListRestauranteDomicilios() {
        return listRestauranteDomicilios;
    }

    public void setListRestauranteDomicilios(List<ServicioDomicilio> listRestauranteDomicilios) {
        this.listRestauranteDomicilios = listRestauranteDomicilios;
    }

    @Override
    public String toString() {
        return "Restaurante{" +
                "nombreComercial='" + nombreComercial + '\'' +
                ", nit='" + nit + '\'' +
                ", direccion='" + direccion + '\'' +
                ", listRestauranteProductos=" + listRestauranteProductos +
                ", listRestauranteClientes=" + listRestauranteClientes +
                ", listRestaurantePedidos=" + listRestaurantePedidos +
                ", listRestauranteDomicilios=" + listRestauranteDomicilios +
                '}';
    }
}

package elbuencomer.app;

import elbuencomer.model.*;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;

public class Main {

    private static final String TITULO = "El Buen Comer";
    private static final Locale CO = Locale.forLanguageTag("es-CO");

    private static Restaurante restaurante;

    /**
     * Se lanza cuando el usuario pulsa "Cancelar" o cierra una ventana,
     * para abandonar la operación en curso y volver al menú principal.
     */
    private static class OperacionCancelada extends RuntimeException {
        private static final long serialVersionUID = 1L;
    }

    public static void main(String[] args) {
        // Botones de las ventanas en español, sin importar el idioma del sistema
        UIManager.put("OptionPane.yesButtonText", "Sí");
        UIManager.put("OptionPane.noButtonText", "No");
        UIManager.put("OptionPane.okButtonText", "Aceptar");
        UIManager.put("OptionPane.cancelButtonText", "Cancelar");

        restaurante = new Restaurante("El Buen Comer", "900.123.456-7", "Armenia, Quindío");
        restaurante.registrarServicioDomicilio(new RapidoExpress());
        restaurante.registrarServicioDomicilio(new EntregaSegura());

        boolean salir = false;
        while (!salir) {
            String entrada = JOptionPane.showInputDialog(null, textoMenu(), TITULO, JOptionPane.PLAIN_MESSAGE);
            if (entrada == null) { // Cancelar o cerrar la ventana del menú
                salir = confirmar("¿Desea salir de la aplicación?");
                continue;
            }
            int opcion;
            try {
                opcion = Integer.parseInt(entrada.trim());
            } catch (NumberFormatException e) {
                error("Ingrese el número de una opción del menú.");
                continue;
            }
            try {
                switch (opcion) {
                    case 1 -> registrarProducto();
                    case 2 -> registrarCliente();
                    case 3 -> registrarPedido();
                    case 4 -> calcularValorTotalPedido();
                    case 5 -> consultarHistorialCliente();
                    case 6 -> marcarPedidoEntregado();
                    case 7 -> cancelarPedido();
                    case 8 -> listarProductosDisponibles();
                    case 9 -> listarProductosNoCumplen();
                    case 10 -> listarClientes();
                    case 11 -> mostrarReporteIngresos();
                    case 12 -> cargarDatosDemostracion();
                    case 0 -> salir = confirmar("¿Desea salir de la aplicación?");
                    default -> error("Opción no válida.");
                }
            } catch (OperacionCancelada e) {
                // El usuario canceló: se vuelve al menú sin hacer nada.
            } catch (IllegalArgumentException | IllegalStateException e) {
                error(e.getMessage());
            }
        }
        JOptionPane.showMessageDialog(null, "¡Hasta pronto!", TITULO, JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }

    private static String textoMenu() {
        return restaurante + "\n\n"
                + "1. Registrar producto\n"
                + "2. Registrar cliente\n"
                + "3. Registrar pedido\n"
                + "4. Calcular valor total de un pedido\n"
                + "5. Consultar historial de pedidos de un cliente\n"
                + "6. Marcar pedido como entregado\n"
                + "7. Cancelar pedido\n"
                + "8. Consultar productos disponibles\n"
                + "9. Consultar productos que no cumplen calidad\n"
                + "10. Listar clientes\n"
                + "11. Reporte de ingresos y ventas\n"
                + "12. Cargar datos de demostración\n"
                + "0. Salir\n\n"
                + "Seleccione una opción:";
    }

    // =====================================================================
    // 1. Registrar producto
    // =====================================================================

    private static void registrarProducto() {
        String tipo = leerOpcion("Tipo de producto:", new String[]{"Plato fuerte", "Bebida", "Postre"});
        String codigo = leerTexto("Código del producto:");
        if (restaurante.buscarProducto(codigo) != null) {
            error("Ya existe un producto con el código " + codigo + ".");
            return;
        }
        String nombre = leerTexto("Nombre:");
        String descripcion = leerTexto("Descripción:");
        double precioBase = leerDouble("Precio base:");
        int tiempo = leerEntero("Tiempo estimado de preparación (minutos):");

        Producto producto;
        switch (tipo) {
            case "Plato fuerte" -> {
                double peso = leerDouble("Peso (gramos):");
                TipoPreparacion tipoPrep = leerOpcion("Tipo de preparación:", TipoPreparacion.values());
                boolean coccion = confirmar("¿El plato superó el control de cocción?");
                producto = new PlatoFuerte(codigo, nombre, descripcion, precioBase, tiempo, peso, tipoPrep, coccion);
            }
            case "Bebida" -> {
                double volumen = leerDouble("Volumen (ml):");
                double tempServicio = leerDouble("Temperatura de servicio (°C):");
                boolean especial = confirmar("¿La bebida requiere preparación especial?");
                TemperaturaBebida tipoTemp = leerOpcion("¿La bebida es fría o caliente?", TemperaturaBebida.values());
                producto = new Bebida(codigo, nombre, descripcion, precioBase, tiempo, volumen, tempServicio, especial, tipoTemp);
            }
            default -> {
                double peso = leerDouble("Peso (gramos):");
                double tempAlm = leerDouble("Temperatura de almacenamiento (°C):");
                boolean refrigeracion = confirmar("¿El postre requiere refrigeración?");
                producto = new Postre(codigo, nombre, descripcion, precioBase, tiempo, peso, tempAlm, refrigeracion);
            }
        }
        restaurante.registrarProducto(producto);
        String mensaje = "Producto registrado.\nPrecio de venta: " + dinero(producto.calcularPrecioVenta());
        if (producto.cumpleCalidad()) {
            info(mensaje);
        } else {
            advertencia(mensaje + "\n\nAtención: el producto NO cumple las condiciones de calidad,\n"
                    + "por lo que no podrá incluirse en pedidos confirmados.");
        }
    }

    // =====================================================================
    // 2. Registrar cliente
    // =====================================================================

    private static void registrarCliente() {
        String documento = leerTexto("Documento de identidad:");
        if (restaurante.buscarCliente(documento) != null) {
            error("Ya existe un cliente con el documento " + documento + ".");
            return;
        }
        String nombre = leerTexto("Nombre completo:");
        String telefono = leerTexto("Teléfono:");
        String correo = leerTexto("Correo electrónico:");
        String direccion = leerTexto("Dirección de residencia:");
        restaurante.registrarCliente(new Cliente(documento, nombre, telefono, correo, direccion));
        info("Cliente registrado correctamente.");
    }

    // =====================================================================
    // 3. Registrar pedido
    // =====================================================================

    private static void registrarPedido() {
        if (restaurante.getListRestauranteProductos().isEmpty()) {
            error("No hay productos en el menú. Registre productos primero.");
            return;
        }
        String documento = leerTexto("Documento del cliente:");
        Cliente cliente = restaurante.buscarCliente(documento);
        if (cliente == null) {
            error("El cliente no está registrado.");
            return;
        }
        String codigo = leerTexto("Código del pedido:");
        if (restaurante.buscarPedido(codigo) != null) {
            error("Ya existe un pedido con el código " + codigo + ".");
            return;
        }
        LocalDate fecha = leerFecha("Fecha del pedido (AAAA-MM-DD).\nDeje vacío para usar la fecha de hoy:");
        ModalidadEntrega modalidad = leerOpcion("Modalidad de entrega:", ModalidadEntrega.values());

        Pedido pedido = new Pedido(codigo, fecha, cliente, modalidad);

        // ---- Selección de productos ----
        boolean seguir = true;
        while (seguir) {
            String codProducto = leerTextoOpcional(listaProductosMenu()
                    + "\nProductos en el pedido: " + resumenPedido(pedido)
                    + "\n\nCódigo del producto a agregar (deje vacío para terminar):");
            if (codProducto.isEmpty()) {
                seguir = false;
            } else {
                Producto producto = restaurante.buscarProducto(codProducto);
                if (producto == null) {
                    error("El producto " + codProducto + " no pertenece al menú.");
                } else {
                    int cantidad = leerEntero("Cantidad de \"" + producto.getNombre() + "\":");
                    try {
                        pedido.agregarProducto(producto, cantidad);
                    } catch (IllegalArgumentException e) {
                        error(e.getMessage());
                    }
                }
            }
        }

        // ---- Domicilio ----
        if (pedido.esDomicilio()) {
            boolean aceptado = false;
            while (!aceptado) {
                List<ServicioDomicilio> empresas = restaurante.getListRestauranteDomicilios();
                ServicioDomicilio empresa = leerOpcion("Empresa de domicilio:",
                        empresas.toArray(new ServicioDomicilio[0]));
                double distancia = leerDouble("Distancia del recorrido (km):");
                aceptado = restaurante.solicitarServicioDomicilio(pedido, empresa, distancia);
                if (aceptado) {
                    info(empresa + " aceptó el servicio.\nCosto del domicilio: " + dinero(pedido.getCostoDomicilio()));
                } else {
                    error(empresa + " no acepta un recorrido de " + distancia + " km.\nElija otra empresa o distancia.");
                }
            }
        }

        // ---- Confirmación ----
        restaurante.confirmarPedido(pedido); // lanza excepción con el motivo si no se puede confirmar
        mostrarTexto("Pedido confirmado", "PEDIDO CONFIRMADO\n\n" + detallePedido(pedido));
    }

    private static String listaProductosMenu() {
        StringBuilder sb = new StringBuilder("Productos del menú:\n");
        for (Producto p : restaurante.getListRestauranteProductos()) {
            sb.append("  ").append(p.getCodigo()).append(" - ").append(p.getNombre())
                    .append("  ").append(dinero(p.calcularPrecioVenta()))
                    .append(p.cumpleCalidad() ? "" : "   (no cumple calidad)")
                    .append("\n");
        }
        return sb.toString();
    }

    private static String resumenPedido(Pedido pedido) {
        if (pedido.getListPedidoProductos().isEmpty()) {
            return "(ninguno)";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < pedido.getListPedidoProductos().size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(pedido.getListPedidoDetalles().get(i).cantidad())
                    .append(" x ").append(pedido.getListPedidoProductos().get(i).getNombre());
        }
        return sb.toString();
    }

    // =====================================================================
    // 4. Calcular valor total
    // =====================================================================

    private static void calcularValorTotalPedido() {
        String codigo = leerTexto("Código del pedido:");
        Pedido pedido = restaurante.buscarPedido(codigo);
        if (pedido == null) {
            error("No existe un pedido con el código " + codigo + ".");
            return;
        }
        mostrarTexto("Valor del pedido " + codigo, detallePedido(pedido));
    }

    private static String detallePedido(Pedido pedido) {
        StringBuilder sb = new StringBuilder();
        sb.append("Código:     ").append(pedido.getCodigo()).append("\n")
                .append("Fecha:      ").append(pedido.getFecha()).append("\n")
                .append("Cliente:    ").append(pedido.getTheClientePedido().getNombreCompleto()).append("\n")
                .append("Modalidad:  ").append(pedido.getModalidad()).append("\n")
                .append("Estado:     ").append(pedido.getEstado()).append("\n\n");
        for (int i = 0; i < pedido.getListPedidoProductos().size(); i++) {
            Producto producto = pedido.getListPedidoProductos().get(i);
            DetallePedido detalle = pedido.getListPedidoDetalles().get(i);
            sb.append(String.format("%-24s %3d x %10s = %12s%n",
                    recortar(producto.getNombre(), 24), detalle.cantidad(),
                    dinero(producto.calcularPrecioVenta()), dinero(pedido.calcularSubtotal(i))));
        }
        sb.append("\nValor productos: ").append(dinero(pedido.calcularValorProductos())).append("\n");
        if (pedido.esDomicilio()) {
            sb.append("Domicilio (").append(pedido.getTheServicioPedido()).append(", ")
                    .append(pedido.getDistanciaKm()).append(" km): ")
                    .append(dinero(pedido.getCostoDomicilio())).append("\n");
        }
        sb.append("VALOR TOTAL: ").append(dinero(restaurante.calcularValorTotalPedido(pedido.getCodigo())));
        return sb.toString();
    }

    // =====================================================================
    // 5. Historial de cliente
    // =====================================================================

    private static void consultarHistorialCliente() {
        String documento = leerTexto("Documento del cliente:");
        List<Pedido> historial = restaurante.consultarHistorialCliente(documento);
        Cliente cliente = restaurante.buscarCliente(documento);
        StringBuilder sb = new StringBuilder("Historial de " + cliente.getNombreCompleto() + "\n\n");
        if (historial.isEmpty()) {
            sb.append("(sin pedidos)");
        } else {
            sb.append(String.format("%-10s %-12s %-12s %14s%n", "Código", "Fecha", "Estado", "Valor total"));
            for (Pedido p : historial) {
                sb.append(String.format("%-10s %-12s %-12s %14s%n",
                        p.getCodigo(), p.getFecha(), p.getEstado(), dinero(p.calcularValorTotal())));
            }
            sb.append("\nTotal comprado (sin cancelados): ").append(dinero(cliente.calcularTotalCompras()));
        }
        mostrarTexto("Historial de pedidos", sb.toString());
    }

    // =====================================================================
    // Estados
    // =====================================================================

    private static void marcarPedidoEntregado() {
        String codigo = leerTexto("Código del pedido a marcar como entregado:");
        restaurante.marcarPedidoEntregado(codigo);
        info("Pedido " + codigo + " marcado como ENTREGADO.");
    }

    private static void cancelarPedido() {
        String codigo = leerTexto("Código del pedido a cancelar:");
        if (!confirmar("¿Seguro que desea cancelar el pedido " + codigo + "?")) {
            return;
        }
        restaurante.cancelarPedido(codigo);
        info("Pedido " + codigo + " CANCELADO.");
    }

    // =====================================================================
    // Consultas
    // =====================================================================

    private static void listarProductosDisponibles() {
        mostrarTexto("Productos disponibles",
                "Productos disponibles (cumplen calidad):\n\n" + textoProductos(restaurante.consultarProductosDisponibles()));
    }

    private static void listarProductosNoCumplen() {
        mostrarTexto("Productos que no cumplen calidad",
                "Productos que NO cumplen las condiciones de calidad:\n\n"
                        + textoProductos(restaurante.consultarProductosNoCumplenCalidad()));
    }

    private static String textoProductos(List<Producto> lista) {
        if (lista.isEmpty()) {
            return "(ninguno)";
        }
        StringBuilder sb = new StringBuilder();
        for (Producto p : lista) {
            sb.append(p).append("\n    Precio de venta: ").append(dinero(p.calcularPrecioVenta())).append("\n\n");
        }
        return sb.toString();
    }

    private static void listarClientes() {
        StringBuilder sb = new StringBuilder("Clientes registrados:\n\n");
        if (restaurante.getListRestauranteClientes().isEmpty()) {
            sb.append("(ninguno)");
        }
        for (Cliente c : restaurante.getListRestauranteClientes()) {
            sb.append(c).append(" | Pedidos: ").append(c.getListClientePedidos().size()).append("\n");
        }
        mostrarTexto("Clientes", sb.toString());
    }

    private static void mostrarReporteIngresos() {
        Producto masVendido = restaurante.obtenerProductoMasVendido();
        String categoria = restaurante.obtenerCategoriaMayoresIngresos();
        StringBuilder sb = new StringBuilder("REPORTE DE INGRESOS\n\n");
        sb.append("Ingresos por ventas (restaurante): ").append(dinero(restaurante.calcularIngresosVentas())).append("\n")
                .append("Valores de domicilios (empresas):  ").append(dinero(restaurante.calcularIngresosDomicilios())).append("\n\n")
                .append("Producto más vendido: ").append(masVendido != null ? masVendido.getNombre() : "(sin ventas)").append("\n")
                .append("Categoría con mayores ingresos: ").append(categoria != null ? categoria : "(sin ventas)").append("\n\n")
                .append("Pedidos registrados: ").append(restaurante.getListRestaurantePedidos().size()).append("\n");
        for (ServicioDomicilio s : restaurante.getListRestauranteDomicilios()) {
            sb.append("  Pedidos atendidos por ").append(s).append(": ").append(s.getListServicioPedidos().size()).append("\n");
        }
        mostrarTexto("Reporte de ingresos", sb.toString());
    }

    // =====================================================================
    // Datos de demostración
    // =====================================================================

    private static void cargarDatosDemostracion() {
        if (restaurante.buscarProducto("P01") != null) {
            info("Los datos de demostración ya fueron cargados.");
            return;
        }
        restaurante.registrarProducto(new PlatoFuerte("P01", "Bandeja paisa", "Frijoles, arroz, carne, chicharrón",
                30000.0, 25, 850, TipoPreparacion.FRITO, true));
        restaurante.registrarProducto(new PlatoFuerte("P02", "Trucha a la plancha", "Trucha con patacón y ensalada",
                28000.0, 20, 450, TipoPreparacion.A_LA_PLANCHA, true));
        restaurante.registrarProducto(new PlatoFuerte("P03", "Costilla asada", "Costilla de cerdo (sin aprobar cocción)",
                32000.0, 45, 600, TipoPreparacion.ASADO, false));
        restaurante.registrarProducto(new Bebida("B01", "Limonada de coco", "Limonada cremosa",
                8000.0, 5, 400, 4, true, TemperaturaBebida.FRIO));
        restaurante.registrarProducto(new Bebida("B02", "Café de origen", "Café del Quindío",
                4000.0, 3, 250, 70, false, TemperaturaBebida.CALIENTE));
        restaurante.registrarProducto(new Postre("D01", "Postre de natas", "Postre tradicional refrigerado",
                9000.0, 15, 200, 4, true));
        restaurante.registrarProducto(new Postre("D02", "Brownie", "Brownie de chocolate",
                7000.0, 20, 150, 20, false));

        Cliente c1 = new Cliente("1094000001", "Laura Gómez", "3101234567", "laura@correo.com", "Cra 14 # 20-10");
        Cliente c2 = new Cliente("1094000002", "Andrés Ríos", "3207654321", "andres@correo.com", "Calle 50 # 12-30");
        restaurante.registrarCliente(c1);
        restaurante.registrarCliente(c2);

        Pedido p1 = new Pedido("PED001", LocalDate.now(), c1, ModalidadEntrega.CONSUMO_RESTAURANTE);
        p1.agregarProducto(restaurante.buscarProducto("P01"), 2);
        p1.agregarProducto(restaurante.buscarProducto("B01"), 2);
        restaurante.confirmarPedido(p1);

        Pedido p2 = new Pedido("PED002", LocalDate.now(), c2, ModalidadEntrega.DOMICILIO);
        p2.agregarProducto(restaurante.buscarProducto("P02"), 1);
        p2.agregarProducto(restaurante.buscarProducto("D01"), 3);
        restaurante.solicitarServicioDomicilio(p2, restaurante.getListRestauranteDomicilios().get(1), 8);
        restaurante.confirmarPedido(p2);
        restaurante.marcarPedidoEntregado("PED002");

        info("Datos de demostración cargados:\n"
                + "- 7 productos (P03 no cumple calidad)\n"
                + "- 2 clientes (1094000001 y 1094000002)\n"
                + "- 2 pedidos (PED001 confirmado y PED002 entregado)");
    }

    // =====================================================================
    // Utilidades de entrada y salida con JOptionPane
    // =====================================================================

    /** Pide un texto obligatorio. Cancelar abandona la operación. */
    private static String leerTexto(String mensaje) {
        while (true) {
            String texto = leerTextoOpcional(mensaje);
            if (!texto.isEmpty()) {
                return texto;
            }
            error("Este campo es obligatorio.");
        }
    }

    /** Pide un texto que puede quedar vacío. Cancelar abandona la operación. */
    private static String leerTextoOpcional(String mensaje) {
        String texto = JOptionPane.showInputDialog(null, mensaje, TITULO, JOptionPane.QUESTION_MESSAGE);
        if (texto == null) {
            throw new OperacionCancelada();
        }
        return texto.trim();
    }

    private static int leerEntero(String mensaje) {
        while (true) {
            try {
                return Integer.parseInt(leerTexto(mensaje));
            } catch (NumberFormatException e) {
                error("Ingrese un número entero válido.");
            }
        }
    }

    private static double leerDouble(String mensaje) {
        while (true) {
            try {
                return Double.parseDouble(leerTexto(mensaje).replace(",", "."));
            } catch (NumberFormatException e) {
                error("Ingrese un número válido.");
            }
        }
    }

    private static LocalDate leerFecha(String mensaje) {
        while (true) {
            String texto = leerTextoOpcional(mensaje);
            if (texto.isEmpty()) {
                return LocalDate.now();
            }
            try {
                return LocalDate.parse(texto);
            } catch (DateTimeParseException e) {
                error("Formato de fecha inválido. Use AAAA-MM-DD (por ejemplo 2026-11-12).");
            }
        }
    }

    /** Muestra una lista desplegable y devuelve la opción elegida. Cancelar abandona la operación. */
    private static <T> T leerOpcion(String mensaje, T[] opciones) {
        Object elegido = JOptionPane.showInputDialog(null, mensaje, TITULO,
                JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);
        if (elegido == null) {
            throw new OperacionCancelada();
        }
        @SuppressWarnings("unchecked")
        T resultado = (T) elegido;
        return resultado;
    }

    /** Pregunta Sí / No. Cerrar la ventana abandona la operación. */
    private static boolean confirmar(String mensaje) {
        int r = JOptionPane.showConfirmDialog(null, mensaje, TITULO, JOptionPane.YES_NO_OPTION);
        if (r == JOptionPane.CLOSED_OPTION) {
            throw new OperacionCancelada();
        }
        return r == JOptionPane.YES_OPTION;
    }

    private static void info(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, TITULO, JOptionPane.INFORMATION_MESSAGE);
    }

    private static void advertencia(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, TITULO, JOptionPane.WARNING_MESSAGE);
    }

    private static void error(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, TITULO + " - Error", JOptionPane.ERROR_MESSAGE);
    }

    /** Muestra textos largos (listas, tablas) en un área con scroll y letra monoespaciada. */
    private static void mostrarTexto(String titulo, String texto) {
        JTextArea area = new JTextArea(texto);
        area.setEditable(false);
        area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        area.setCaretPosition(0);
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(720, 380));
        JOptionPane.showMessageDialog(null, scroll, titulo, JOptionPane.INFORMATION_MESSAGE);
    }

    private static String recortar(String texto, int max) {
        return texto.length() <= max ? texto : texto.substring(0, max - 1) + "…";
    }

    private static String dinero(double valor) {
        return String.format(CO, "$%,.0f", valor);
    }
}

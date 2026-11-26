package mercado67.modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ServicioPedidos {

    private static final String ARCHIVO_PEDIDOS = "data/pedidos.json";

    private final ListaEnlazada<Pedido> pedidos = new ListaEnlazada<>();
    private int siguienteId = 1;

    public ServicioPedidos() {
        cargarDesdeJSON();
    }

    public Pedido crearPedido(Usuario comprador, Carrito carrito) {
        List<ItemCarrito> copia = new ArrayList<>();
        for (ItemCarrito item : carrito.getItems()) {
            copia.add(new ItemCarrito(item.getProducto(), item.getCantidad()));
        }
        Pedido pedido = new Pedido(siguienteId, comprador.getNombre().hashCode(), LocalDate.now(), "Pagado", copia);
        siguienteId++;
        pedidos.agregar(pedido);
        guardarEnJSON();
        return pedido;
    }

    public ListaEnlazada<Pedido> pedidosDe(Usuario comprador) {
        ListaEnlazada<Pedido> resultado = new ListaEnlazada<>();
        int id = comprador.getNombre().hashCode();
        for (Pedido p : pedidos) {
            if (p.getIdComprador() == id) {
                resultado.agregar(p);
            }
        }
        return resultado;
    }

    public ListaEnlazada<Pedido> todos() {
        return pedidos;
    }

    private void cargarDesdeJSON() {
        String contenido = UtilJSON.leerArchivo(ARCHIVO_PEDIDOS);
        if (contenido == null || contenido.trim().isEmpty()) {
            return;
        }
        Object parseado = UtilJSON.parsearJSON(contenido);
        if (!(parseado instanceof List)) {
            return;
        }
        List<?> lista = (List<?>) parseado;
        int maxId = 0;
        for (Object item : lista) {
            if (!(item instanceof Map)) {
                continue;
            }
            @SuppressWarnings("unchecked")
            Map<String, Object> m = (Map<String, Object>) item;
            int id = obtenerInt(m, "idPedido", 0);
            int idComprador = obtenerInt(m, "idComprador", 0);
            String estado = obtenerString(m, "estado");
            if (estado == null) {
                estado = "Pagado";
            }
            LocalDate fecha = LocalDate.now();
            String fechaTexto = obtenerString(m, "fecha");
            if (fechaTexto != null) {
                try {
                    fecha = LocalDate.parse(fechaTexto);
                } catch (Exception e) {
                    // Si la fecha guardada no es valida se usa la fecha actual.
                }
            }
            List<ItemCarrito> items = new ArrayList<>();
            Object itemsGuardados = m.get("items");
            if (itemsGuardados instanceof List) {
                for (Object itemGuardado : (List<?>) itemsGuardados) {
                    if (!(itemGuardado instanceof Map)) {
                        continue;
                    }
                    @SuppressWarnings("unchecked")
                    Map<String, Object> im = (Map<String, Object>) itemGuardado;
                    int idProducto = obtenerInt(im, "idProducto", 0);
                    String nombre = obtenerString(im, "nombre");
                    if (nombre == null) {
                        nombre = "Producto";
                    }
                    double precio = obtenerDouble(im, "precio", 0);
                    int cantidad = obtenerInt(im, "cantidad", 0);
                    String descripcion = obtenerString(im, "descripcion");
                    if (descripcion == null) {
                        descripcion = "";
                    }
                    int idVendedor = obtenerInt(im, "idVendedor", 0);
                    Producto producto = new Producto(idProducto, nombre, precio, 0, descripcion, idVendedor);
                    items.add(new ItemCarrito(producto, cantidad));
                }
            }
            Pedido pedido = new Pedido(id, idComprador, fecha, estado, items);
            pedidos.agregar(pedido);
            if (id > maxId) {
                maxId = id;
            }
        }
        siguienteId = maxId + 1;
    }

    private void guardarEnJSON() {
        List<Map<String, Object>> lista = new ArrayList<>();
        for (Pedido p : pedidos) {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("idPedido", p.getIdPedido());
            m.put("idComprador", p.getIdComprador());
            m.put("fecha", p.getFecha() == null ? null : p.getFecha().toString());
            m.put("estado", p.getEstado());
            List<Map<String, Object>> items = new ArrayList<>();
            for (ItemCarrito item : p.getItemsComprados()) {
                Producto producto = item.getProducto();
                Map<String, Object> im = new LinkedHashMap<>();
                im.put("idProducto", producto.getIdProducto());
                im.put("nombre", producto.getNombre());
                im.put("precio", producto.getPrecio());
                im.put("cantidad", item.getCantidad());
                im.put("descripcion", producto.getDescripcion());
                im.put("idVendedor", producto.getIdVendedor());
                items.add(im);
            }
            m.put("items", items);
            lista.add(m);
        }
        UtilJSON.escribirArchivo(ARCHIVO_PEDIDOS, UtilJSON.listaAJSON(lista));
    }

    private static int obtenerInt(Map<String, Object> m, String clave, int predeterminado) {
        Object v = m.get(clave);
        if (v instanceof Number) {
            return ((Number) v).intValue();
        }
        if (v != null) {
            try {
                return Integer.parseInt(v.toString());
            } catch (NumberFormatException e) {
                return predeterminado;
            }
        }
        return predeterminado;
    }

    private static double obtenerDouble(Map<String, Object> m, String clave, double predeterminado) {
        Object v = m.get(clave);
        if (v instanceof Number) {
            return ((Number) v).doubleValue();
        }
        if (v != null) {
            try {
                return Double.parseDouble(v.toString());
            } catch (NumberFormatException e) {
                return predeterminado;
            }
        }
        return predeterminado;
    }

    private static String obtenerString(Map<String, Object> m, String clave) {
        Object v = m.get(clave);
        return v == null ? null : v.toString();
    }
}

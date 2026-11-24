/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mercado67.modelo;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ServicioProductos {

    private static final String ARCHIVO_PRODUCTOS = "data/productos.json";

    private final ListaEnlazada<Producto> productos = new ListaEnlazada<>();
    private int siguienteId = 1;

    public ServicioProductos() {
        siguienteId = 1;
        cargarDesdeJSON();
        if (productos.tamano() == 0) {
            cargarDatosPredeterminados();
            guardarEnJSON();
        }
    }

    private void cargarDatosPredeterminados() {
        productos.agregar(new Producto(1, "Audifonos", 85000, 10, "Audifonos inalambricos", 1));
        productos.agregar(new Producto(2, "Mouse gamer", 60000, 15, "Mouse con luces RGB", 1));
        productos.agregar(new Producto(3, "Teclado mecanico", 120000, 8, "Teclado con switches rojos", 1));
        productos.agregar(new Producto(4, "Cuaderno", 8000, 50, "Cuaderno de 100 hojas", 1));
        productos.agregar(new Producto(5, "Mochila", 95000, 12, "Mochila resistente al agua", 1));
        productos.agregar(new Producto(6, "Botella termica", 35000, 20, "Botella de acero 750 ml", 1));
        productos.agregar(new Producto(7, "Lampara de escritorio", 45000, 9, "Lampara LED regulable", 1));
        productos.agregar(new Producto(8, "Libreta de notas", 12000, 30, "Libreta de 80 hojas", 1));
        siguienteId = 9;
    }

    private void cargarDesdeJSON() {
        String contenido = UtilJSON.leerArchivo(ARCHIVO_PRODUCTOS);
        if (contenido == null || contenido.trim().isEmpty()) {
            return;
        }
        Object parseado = UtilJSON.parsearJSON(contenido);
        if (!(parseado instanceof List)) {
            return;
        }
        @SuppressWarnings("unchecked")
        List<Object> lista = (List<Object>) parseado;
        int maxId = 0;
        for (Object item : lista) {
            if (item instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> m = (Map<String, Object>) item;
                int id = obtenerInt(m, "idProducto", 0);
                String nombre = obtenerString(m, "nombre");
                double precio = obtenerDouble(m, "precio", 0);
                int cantidad = obtenerInt(m, "cantidad", 0);
                String descripcion = obtenerString(m, "descripcion");
                int idVendedor = obtenerInt(m, "idVendedor", 0);
                if (nombre != null && descripcion != null) {
                    Producto p = new Producto(id, nombre, precio, cantidad, descripcion, idVendedor);
                    @SuppressWarnings("unchecked")
                    List<String> fotos = (List<String>) m.get("rutasFotos");
                    if (fotos != null) {
                        for (String f : fotos) {
                            p.agregarFoto(f);
                        }
                    }
                    productos.agregar(p);
                    if (id > maxId) {
                        maxId = id;
                    }
                }
            }
        }
        siguienteId = maxId + 1;
    }

    private void guardarEnJSON() {
        List<Map<String, Object>> lista = new ArrayList<>();
        for (Producto p : productos) {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("idProducto", p.getIdProducto());
            m.put("nombre", p.getNombre());
            m.put("precio", p.getPrecio());
            m.put("cantidad", p.getCantidad());
            m.put("descripcion", p.getDescripcion());
            m.put("idVendedor", p.getIdVendedor());
            m.put("rutasFotos", p.getRutasFotos());
            lista.add(m);
        }
        UtilJSON.escribirArchivo(ARCHIVO_PRODUCTOS, UtilJSON.listaAJSON(lista));
    }

    private static int obtenerInt(Map<String, Object> m, String clave, int defaultValue) {
        Object v = m.get(clave);
        if (v instanceof Number) {
            return ((Number) v).intValue();
        }
        if (v != null) {
            try {
                return Integer.parseInt(v.toString());
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }

    private static double obtenerDouble(Map<String, Object> m, String clave, double defaultValue) {
        Object v = m.get(clave);
        if (v instanceof Number) {
            return ((Number) v).doubleValue();
        }
        if (v != null) {
            try {
                return Double.parseDouble(v.toString());
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }

    private static String obtenerString(Map<String, Object> m, String clave) {
        Object v = m.get(clave);
        return v == null ? null : v.toString();
    }

    public void agregarProducto(Producto producto) {
        if (producto.getIdProducto() == 0) {
            producto.setIdProducto(siguienteId++);
        } else if (producto.getIdProducto() >= siguienteId) {
            siguienteId = producto.getIdProducto() + 1;
        }
        productos.agregar(producto);
        guardarEnJSON();
    }

    public ListaEnlazada<Producto> listarProductos() {
        return productos;
    }

    public Producto buscarPorId(int id) {
        for (Producto p : productos) {
            if (p.getIdProducto() == id) {
                return p;
            }
        }
        return null;
    }

    public List<Producto> buscarPorNombre(String texto) {
        List<Producto> resultados = new ArrayList<>();
        if (texto == null || texto.isEmpty()) {
            return resultados;
        }
        String busqueda = texto.toLowerCase();
        for (Producto p : productos) {
            if (p.getNombre().toLowerCase().contains(busqueda)) {
                resultados.add(p);
            }
        }
        return resultados;
    }

    public boolean eliminarProducto(int id) {
        for (int i = 0; i < productos.tamano(); i++) {
            if (productos.obtener(i).getIdProducto() == id) {
                boolean resultado = productos.eliminar(i);
                if (resultado) {
                    guardarEnJSON();
                }
                return resultado;
            }
        }
        return false;
    }

    public int cantidadProductos() {
        return productos.tamano();
    }
}

package mercado67.modelo;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Guarda los productos favoritos (lista de deseos) de cada usuario en
 * data/deseos.json. Cada deseo se identifica por el correo del usuario y el
 * id del producto.
 */
public class ServicioDeseos {

    private static final String ARCHIVO_DESEOS = "data/deseos.json";

    private final Map<String, Set<Integer>> deseos = new LinkedHashMap<>();

    public ServicioDeseos() {
        cargarDesdeJSON();
    }

    private void cargarDesdeJSON() {
        String contenido = UtilJSON.leerArchivo(ARCHIVO_DESEOS);
        if (contenido == null || contenido.trim().isEmpty()) {
            return;
        }
        Object parseado = UtilJSON.parsearJSON(contenido);
        if (!(parseado instanceof List)) {
            return;
        }
        for (Object item : (List<?>) parseado) {
            if (item instanceof Map) {
                Map<?, ?> m = (Map<?, ?>) item;
                Object correoObj = m.get("correo");
                Object idObj = m.get("idProducto");
                if (correoObj != null && idObj instanceof Number) {
                    String correo = correoObj.toString();
                    int idProducto = ((Number) idObj).intValue();
                    deseos.computeIfAbsent(correo, k -> new LinkedHashSet<>()).add(idProducto);
                }
            }
        }
    }

    private void guardarEnJSON() {
        List<Map<String, Object>> lista = new ArrayList<>();
        for (Map.Entry<String, Set<Integer>> entrada : deseos.entrySet()) {
            for (Integer idProducto : entrada.getValue()) {
                Map<String, Object> item = new LinkedHashMap<>();
                item.put("correo", entrada.getKey());
                item.put("idProducto", idProducto);
                lista.add(item);
            }
        }
        UtilJSON.escribirArchivo(ARCHIVO_DESEOS, UtilJSON.listaAJSON(lista));
    }

    /**
     * Agrega un producto a la lista de deseos del usuario.
     * Retorna true si se agrego y false si ya estaba.
     */
    public boolean agregar(String correo, int idProducto) {
        if (correo == null || correo.isEmpty()) {
            return false;
        }
        Set<Integer> ids = deseos.computeIfAbsent(correo, k -> new LinkedHashSet<>());
        boolean nuevo = ids.add(idProducto);
        if (nuevo) {
            guardarEnJSON();
        }
        return nuevo;
    }

    /**
     * Quita un producto de la lista de deseos del usuario.
     */
    public boolean quitar(String correo, int idProducto) {
        Set<Integer> ids = deseos.get(correo);
        if (ids == null) {
            return false;
        }
        boolean eliminado = ids.remove(idProducto);
        if (eliminado) {
            if (ids.isEmpty()) {
                deseos.remove(correo);
            }
            guardarEnJSON();
        }
        return eliminado;
    }

    public List<Integer> obtenerIds(String correo) {
        List<Integer> lista = new ArrayList<>();
        Set<Integer> ids = deseos.get(correo);
        if (ids != null) {
            lista.addAll(ids);
        }
        return lista;
    }

    public boolean estaEnDeseos(String correo, int idProducto) {
        Set<Integer> ids = deseos.get(correo);
        return ids != null && ids.contains(idProducto);
    }

    public int cantidad(String correo) {
        Set<Integer> ids = deseos.get(correo);
        return (ids == null) ? 0 : ids.size();
    }
}

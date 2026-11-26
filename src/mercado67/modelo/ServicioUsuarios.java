package mercado67.modelo;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ServicioUsuarios {

    private static final String ARCHIVO_USUARIOS = "data/usuarios.json";

    private final List<Usuario> usuarios = new ArrayList<>();

    public ServicioUsuarios() {
        cargarDesdeJSON();
        if (usuarios.isEmpty()) {
            usuarios.add(new Usuario("Administrador", "admin@mercado67.com", "admin123", true));
            usuarios.add(new Usuario("Cliente de prueba", "cliente@mercado67.com", "1234", false));
            guardarEnJSON();
        }
    }

    private void cargarDesdeJSON() {
        String contenido = UtilJSON.leerArchivo(ARCHIVO_USUARIOS);
        if (contenido == null || contenido.trim().isEmpty()) {
            return;
        }
        Object parseado = UtilJSON.parsearJSON(contenido);
        if (!(parseado instanceof List)) {
            return;
        }
        List<?> lista = (List<?>) parseado;
        for (Object item : lista) {
            if (item instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> m = (Map<String, Object>) item;
                String nombre = obtenerString(m, "nombre");
                String correo = obtenerString(m, "correo");
                String contraseña = obtenerString(m, "contraseña");
                boolean admin = obtenerBooleano(m, "admin", false);
                boolean activo = obtenerBooleano(m, "activo", true);
                if (nombre != null && correo != null && contraseña != null) {
                    usuarios.add(new Usuario(nombre, correo, contraseña, admin, activo));
                }
            }
        }
    }

    private void guardarEnJSON() {
        List<Map<String, Object>> lista = new ArrayList<>();
        for (Usuario u : usuarios) {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("nombre", u.getNombre());
            m.put("correo", u.getCorreo());
            m.put("contraseña", u.getContraseña());
            m.put("admin", u.isAdmin());
            m.put("activo", u.isActivo());
            lista.add(m);
        }
        UtilJSON.escribirArchivo(ARCHIVO_USUARIOS, UtilJSON.listaAJSON(lista));
    }

    private static String obtenerString(Map<String, Object> m, String clave) {
        Object v = m.get(clave);
        return v == null ? null : v.toString();
    }

    private static boolean obtenerBooleano(Map<String, Object> m, String clave, boolean predeterminado) {
        Object v = m.get(clave);
        if (v instanceof Boolean) {
            return (Boolean) v;
        }
        if (v instanceof String) {
            return Boolean.parseBoolean((String) v);
        }
        return predeterminado;
    }

    public Usuario iniciarSesion(String correo, String contrasena) {
        for (Usuario u : usuarios) {
            if (u.isActivo() && u.validar(correo, contrasena)) {
                return u;
            }
        }
        return null;
    }

    /**
     * Indica si las credenciales son correctas pero la cuenta esta desactivada.
     */
    public boolean estaDesactivada(String correo, String contrasena) {
        for (Usuario u : usuarios) {
            if (!u.isActivo() && u.validar(correo, contrasena)) {
                return true;
            }
        }
        return false;
    }

    public boolean existeCorreo(String correo) {
        for (Usuario u : usuarios) {
            if (u.getCorreo().equalsIgnoreCase(correo)) {
                return true;
            }
        }
        return false;
    }

    public boolean registrar(String nombre, String correo, String contrasena) {
        if (nombre == null || nombre.isEmpty() || correo == null || correo.isEmpty()
                || contrasena == null || contrasena.isEmpty()) {
            return false;
        }
        if (existeCorreo(correo)) {
            return false;
        }
        usuarios.add(new Usuario(nombre, correo, contrasena, false));
        guardarEnJSON();
        return true;
    }

    public boolean establecerActivo(Usuario usuario, boolean activo) {
        if (usuario == null || usuario.isAdmin()) {
            return false;
        }
        usuario.setActivo(activo);
        guardarEnJSON();
        return true;
    }

    public int cantidadUsuarios() {
        return usuarios.size();
    }

    public List<Usuario> obtenerTodos() {
        return new ArrayList<>(usuarios);
    }
}

package mercado67.modelo;

public class SesionActiva {

    private static final ServicioUsuarios servicioUsuarios = new ServicioUsuarios();
    private static final ServicioProductos servicioProductos = new ServicioProductos();
    private static Usuario usuarioActual;

    public static ServicioUsuarios getServicioUsuarios() {
        return servicioUsuarios;
    }

    public static ServicioProductos getServicioProductos() {
        return servicioProductos;
    }

    public static Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public static void setUsuarioActual(Usuario usuario) {
        usuarioActual = usuario;
    }

    public static void cerrarSesion() {
        usuarioActual = null;
    }
}
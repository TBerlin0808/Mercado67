package mercado67.modelo;

public class SesionActiva {

    private static final ServicioUsuarios servicioUsuarios = new ServicioUsuarios();
    private static final ServicioProductos servicioProductos = new ServicioProductos();
    private static final ServicioDeseos servicioDeseos = new ServicioDeseos();
    private static Usuario usuarioActual;
    private static Carrito carrito = new Carrito();
    private static Producto productoSeleccionado;

    private static SesionActiva instancia;

    private SesionActiva() {
    }

    public static SesionActiva getInstancia() {
        if (instancia == null) {
            instancia = new SesionActiva();
        }
        return instancia;
    }

    public static ServicioUsuarios getServicioUsuarios() {
        return servicioUsuarios;
    }

    public static ServicioProductos getServicioProductos() {
        return servicioProductos;
    }

    public static ServicioDeseos getServicioDeseos() {
        return servicioDeseos;
    }

    public static Carrito getCarrito() {
        return carrito;
    }

    public static Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public static void setUsuarioActual(Usuario usuario) {
        usuarioActual = usuario;
    }

    public static Producto getProductoSeleccionado() {
        return productoSeleccionado;
    }

    public static void setProductoSeleccionado(Producto producto) {
        productoSeleccionado = producto;
    }

    public static void cerrarSesion() {
        usuarioActual = null;
        productoSeleccionado = null;
        carrito = new Carrito();
    }
}

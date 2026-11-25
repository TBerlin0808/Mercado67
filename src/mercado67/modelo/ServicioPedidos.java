package mercado67.modelo;

public class ServicioPedidos {

    private final ListaEnlazada<Pedido> pedidos = new ListaEnlazada<>();
    private int siguienteId = 1;

    public Pedido crearPedido(Usuario comprador, Carrito carrito) {
        java.util.List<ItemCarrito> copia = new java.util.ArrayList<>();
        for (ItemCarrito item : carrito.getItems()) {
            copia.add(new ItemCarrito(item.getProducto(), item.getCantidad()));
        }
        Pedido pedido = new Pedido(siguienteId, comprador.getNombre().hashCode(), java.time.LocalDate.now(), "Pagado", copia);
        siguienteId++;
        pedidos.agregar(pedido);
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
}
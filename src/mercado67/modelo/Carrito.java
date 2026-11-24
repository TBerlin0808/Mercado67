package mercado67.modelo;

public class Carrito {

    private final ListaEnlazada<ItemCarrito> items = new ListaEnlazada<>();

    public void agregar(Producto producto) {
        int posicion = 0;
        for (ItemCarrito item : items) {
            if (item.getProducto().getIdProducto() == producto.getIdProducto()) {
                item.setCantidad(item.getCantidad() + 1);
                return;
            }
            posicion++;
        }
        items.agregar(new ItemCarrito(producto, 1));
    }

    public void agregar(Producto producto, int cantidad) {
        for (int i = 0; i < cantidad; i++) {
            agregar(producto);
        }
    }

    public boolean quitar(int posicion) {
        return items.eliminar(posicion);
    }

    public double getTotal() {
        double total = 0;
        for (ItemCarrito item : items) {
            total += item.getSubtotal();
        }
        return total;
    }

    public ListaEnlazada<ItemCarrito> getItems() {
        return items;
    }

    public int cantidadItems() {
        return items.tamano();
    }

    public void vaciar() {
        while (items.tamano() > 0) {
            items.eliminar(0);
        }
    }
}
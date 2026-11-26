package mercado67.modelo;

public class Carrito {

    private final ListaEnlazada<ItemCarrito> items = new ListaEnlazada<>();

    public boolean agregar(Producto producto) {
        return agregar(producto, 1);
    }

    public boolean agregar(Producto producto, int cantidad) {
        if (producto == null || cantidad <= 0) {
            return false;
        }
        int enCarrito = cantidadDe(producto.getIdProducto());
        if (enCarrito + cantidad > producto.getCantidad()) {
            return false;
        }
        if (enCarrito > 0) {
            for (ItemCarrito item : items) {
                if (item.getProducto().getIdProducto() == producto.getIdProducto()) {
                    item.setCantidad(item.getCantidad() + cantidad);
                    return true;
                }
            }
        }
        items.agregar(new ItemCarrito(producto, cantidad));
        return true;
    }

    public int cantidadDe(int idProducto) {
        for (ItemCarrito item : items) {
            if (item.getProducto().getIdProducto() == idProducto) {
                return item.getCantidad();
            }
        }
        return 0;
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

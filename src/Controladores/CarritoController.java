package Controladores;

import vista.Navegacion;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import mercado67.modelo.Carrito;
import mercado67.modelo.ItemCarrito;
import mercado67.modelo.Pedido;
import mercado67.modelo.Producto;
import mercado67.modelo.SesionActiva;
import mercado67.modelo.Usuario;

public class CarritoController implements Initializable {

    private static final int ITEMS_POR_PAGINA = 3;

    @FXML private Label lblResultados;
    @FXML private Label lblSubtotal;
    @FXML private Label lblEnvio;
    @FXML private Label lblTotal;
    @FXML private Label lblPaginaPedidos;
    @FXML private Label lblTotalPaginasPedidos;
    @FXML private Button btnAnteriorPedidos;
    @FXML private Button btnSiguientePedidos;

    @FXML private Pane fila1;
    @FXML private Pane fila2;
    @FXML private Pane fila3;

    @FXML private ImageView imgProducto1;
    @FXML private ImageView imgProducto2;
    @FXML private ImageView imgProducto3;

    @FXML private Label lblNombre1;
    @FXML private Label lblNombre2;
    @FXML private Label lblNombre3;

    @FXML private Label lblDetalle1;
    @FXML private Label lblDetalle2;
    @FXML private Label lblDetalle3;

    @FXML private Label lblCantidad1;
    @FXML private Label lblCantidad2;
    @FXML private Label lblCantidad3;

    @FXML private Label lblSubtotal1;
    @FXML private Label lblSubtotal2;
    @FXML private Label lblSubtotal3;

    private int pagina = 0;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        actualizarCarrito();
    }

    private void actualizarCarrito() {
        Carrito carrito = SesionActiva.getCarrito();
        int totalItems = carrito.cantidadItems();
        int totalPaginas = Math.max(1, (totalItems + ITEMS_POR_PAGINA - 1) / ITEMS_POR_PAGINA);
        if (pagina >= totalPaginas) {
            pagina = totalPaginas - 1;
        }
        if (pagina < 0) {
            pagina = 0;
        }

        mostrarFila(fila1, imgProducto1, lblNombre1, lblDetalle1, lblCantidad1, lblSubtotal1, pagina * ITEMS_POR_PAGINA);
        mostrarFila(fila2, imgProducto2, lblNombre2, lblDetalle2, lblCantidad2, lblSubtotal2, pagina * ITEMS_POR_PAGINA + 1);
        mostrarFila(fila3, imgProducto3, lblNombre3, lblDetalle3, lblCantidad3, lblSubtotal3, pagina * ITEMS_POR_PAGINA + 2);

        int total = (int) carrito.getTotal();
        lblResultados.setText(totalItems == 0 ? "El carrito esta vacio" : "Productos distintos: " + totalItems);
        lblSubtotal.setText(String.valueOf(total));
        lblEnvio.setText("0");
        lblTotal.setText(String.valueOf(total));
        lblPaginaPedidos.setText(String.valueOf(pagina + 1));
        lblTotalPaginasPedidos.setText(String.valueOf(totalPaginas));
        btnAnteriorPedidos.setDisable(pagina == 0);
        btnSiguientePedidos.setDisable(pagina >= totalPaginas - 1);
    }

    private void mostrarFila(Pane fila, ImageView img, Label nombre, Label detalle, Label cantidad, Label subtotal, int posicion) {
        ItemCarrito item = SesionActiva.getCarrito().getItems().obtener(posicion);
        if (item == null) {
            fila.setVisible(false);
            return;
        }
        fila.setVisible(true);
        Producto p = item.getProducto();
        String agotado = p.getCantidad() <= 0 ? " (Agotado)" : "";
        nombre.setText(p.getNombre() + agotado);
        detalle.setText("$" + (int) p.getPrecio() + " c/u");
        cantidad.setText(String.valueOf(item.getCantidad()));
        subtotal.setText("$" + (int) item.getSubtotal());
        img.setImage(cargarFoto(p));
    }

    private Image cargarFoto(Producto p) {
        if (p.getRutasFotos() == null || p.getRutasFotos().isEmpty()) {
            return null;
        }
        File archivo = new File(p.getRutasFotos().get(0));
        if (!archivo.exists()) {
            return null;
        }
        Image imagen = new Image(archivo.toURI().toString());
        return imagen.isError() ? null : imagen;
    }

    private void cambiarCantidad(int posicionVista, int delta) {
        int posicion = pagina * ITEMS_POR_PAGINA + posicionVista;
        ItemCarrito item = SesionActiva.getCarrito().getItems().obtener(posicion);
        if (item == null) {
            return;
        }
        int nueva = item.getCantidad() + delta;
        if (nueva <= 0) {
            SesionActiva.getCarrito().quitar(posicion);
        } else if (delta > 0 && nueva > item.getProducto().getCantidad()) {
            mostrar(Alert.AlertType.WARNING, "Stock insuficiente",
                    "No hay mas unidades disponibles de " + item.getProducto().getNombre() + ".");
            return;
        } else {
            item.setCantidad(nueva);
        }
        actualizarCarrito();
    }

    @FXML private void mas1() { cambiarCantidad(0, 1); }
    @FXML private void mas2() { cambiarCantidad(1, 1); }
    @FXML private void mas3() { cambiarCantidad(2, 1); }
    @FXML private void menos1() { cambiarCantidad(0, -1); }
    @FXML private void menos2() { cambiarCantidad(1, -1); }
    @FXML private void menos3() { cambiarCantidad(2, -1); }

    @FXML private void quitar1() { quitar(0); }
    @FXML private void quitar2() { quitar(1); }
    @FXML private void quitar3() { quitar(2); }

    private void quitar(int posicionVista) {
        int posicion = pagina * ITEMS_POR_PAGINA + posicionVista;
        if (posicion >= SesionActiva.getCarrito().cantidadItems()) {
            return;
        }
        SesionActiva.getCarrito().quitar(posicion);
        actualizarCarrito();
    }

    @FXML
    private void paginaAnterior() {
        if (pagina > 0) {
            pagina--;
            actualizarCarrito();
        }
    }

    @FXML
    private void paginaSiguiente() {
        int totalPaginas = Math.max(1,
                (SesionActiva.getCarrito().cantidadItems() + ITEMS_POR_PAGINA - 1) / ITEMS_POR_PAGINA);
        if (pagina < totalPaginas - 1) {
            pagina++;
            actualizarCarrito();
        }
    }

    @FXML
    private void irMiCuenta() {
        Navegacion.mostrar((Stage) lblResultados.getScene().getWindow(), "/vista/MicuentaMisdatos.fxml");
    }

    @FXML
    private void regresar() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/vista/Interfaz de usuario.fxml"));
            Stage stage = (Stage) lblResultados.getScene().getWindow();
            Navegacion.mostrar(stage, root);
        } catch (Exception e) {
            mostrar(Alert.AlertType.ERROR, "Error", "No se pudo regresar a la pantalla principal.");
        }
    }

       @FXML
    private void pagar() {
        if (SesionActiva.getCarrito().cantidadItems() == 0) {
            mostrar(Alert.AlertType.WARNING, "Carrito vacio", "No hay productos en el carrito.");
            return;
        }
        Navegacion.mostrar((Stage) lblResultados.getScene().getWindow(), "/vista/Pago.fxml");
    }

    private void mostrar(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
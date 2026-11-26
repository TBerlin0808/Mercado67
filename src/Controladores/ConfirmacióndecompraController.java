package Controladores;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import mercado67.modelo.ItemCarrito;
import mercado67.modelo.ListaEnlazada;
import mercado67.modelo.Pedido;
import mercado67.modelo.SesionActiva;
import vista.Navegacion;

public class ConfirmacióndecompraController implements Initializable {

    @FXML private Label lblResultados;
    @FXML private Label lblPedido;
    @FXML private Label lblFecha;
    @FXML private Label lblTotal;
    @FXML private Label lblNombre1;
    @FXML private Label lblNombre2;
    @FXML private Label lblNombre3;
    @FXML private Label lblDetalle1;
    @FXML private Label lblDetalle2;
    @FXML private Label lblDetalle3;
    @FXML private Label lblSubtotal1;
    @FXML private Label lblSubtotal2;
    @FXML private Label lblSubtotal3;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (SesionActiva.getUsuarioActual() == null) {
            return;
        }
        ListaEnlazada<Pedido> mios = SesionActiva.getServicioPedidos().pedidosDe(SesionActiva.getUsuarioActual());
        Pedido ultimo = mios.obtener(mios.tamano() - 1);
        if (ultimo == null) {
            return;
        }
        lblPedido.setText(String.valueOf(ultimo.getIdPedido()));
        lblFecha.setText(ultimo.getFecha().toString());
        lblTotal.setText("$" + (int) ultimo.getTotal());
        mostrar(ultimo, lblNombre1, lblDetalle1, lblSubtotal1, 0);
        mostrar(ultimo, lblNombre2, lblDetalle2, lblSubtotal2, 1);
        mostrar(ultimo, lblNombre3, lblDetalle3, lblSubtotal3, 2);
    }

    private void mostrar(Pedido pedido, Label nombre, Label detalle, Label subtotal, int posicion) {
        if (posicion >= pedido.getItemsComprados().size()) {
            return;
        }
        ItemCarrito item = pedido.getItemsComprados().get(posicion);
        nombre.setText(item.getProducto().getNombre());
        detalle.setText(item.getCantidad() + " x $" + (int) item.getProducto().getPrecio());
        subtotal.setText("$" + (int) item.getSubtotal());
    }

    @FXML
    private void irCarrito() {
        Navegacion.mostrar(stage(), "/vista/Carrito.fxml");
    }

    @FXML
    private void irMiCuenta() {
        Navegacion.mostrar(stage(), "/vista/MicuentaMisdatos.fxml");
    }

    @FXML
    private void verCompras() {
        Navegacion.mostrar(stage(), "/vista/MicuentaHistorialdecompras.fxml");
    }

    @FXML
    private void regresar() {
        Navegacion.mostrar(stage(), "/vista/Interfaz de usuario.fxml");
    }

    private Stage stage() {
        return (Stage) lblResultados.getScene().getWindow();
    }
}
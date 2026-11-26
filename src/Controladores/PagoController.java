package Controladores;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mercado67.modelo.Carrito;
import mercado67.modelo.SesionActiva;
import mercado67.modelo.Usuario;
import vista.Navegacion;

public class PagoController implements Initializable {

    @FXML private Label lblResultados;
    @FXML private Label lblSubtotal;
    @FXML private Label lblEnvio;
    @FXML private Label lblTotal;
    @FXML private TextField txtDireccion;
    @FXML private TextField txtCiudad;
    @FXML private TextField txtTelefono;
    @FXML private RadioButton rbTarjeta;
    @FXML private RadioButton rbPse;
    @FXML private RadioButton rbContraEntrega;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        int total = (int) SesionActiva.getCarrito().getTotal();
        lblSubtotal.setText("$" + total);
        lblEnvio.setText("$0");
        lblTotal.setText("$" + total);
    }

    @FXML
    private void confirmar() {
        Carrito carrito = SesionActiva.getCarrito();
        Usuario comprador = SesionActiva.getUsuarioActual();
        if (comprador == null) {
            mostrar(Alert.AlertType.ERROR, "Error", "Debes iniciar sesion para comprar.");
            return;
        }
        if (carrito.cantidadItems() == 0) {
            mostrar(Alert.AlertType.WARNING, "Carrito vacio", "No hay productos en el carrito.");
            return;
        }
        if (txtDireccion.getText().trim().isEmpty() || txtCiudad.getText().trim().isEmpty() || txtTelefono.getText().trim().isEmpty()) {
            mostrar(Alert.AlertType.WARNING, "Datos incompletos", "Completa direccion, ciudad y telefono.");
            return;
        }
        if (!rbTarjeta.isSelected() && !rbPse.isSelected() && !rbContraEntrega.isSelected()) {
            mostrar(Alert.AlertType.WARNING, "Metodo de pago", "Elige un metodo de pago.");
            return;
        }
        String sinStock = SesionActiva.getServicioProductos().validarStock(carrito);
        if (sinStock != null) {
            mostrar(Alert.AlertType.WARNING, "Stock insuficiente", "No hay suficiente stock de " + sinStock + ". Ajusta la cantidad en el carrito.");
            return;
        }
        SesionActiva.getServicioPedidos().crearPedido(comprador, carrito);
        SesionActiva.getServicioProductos().descontarStock(carrito);
        carrito.vaciar();
        Navegacion.mostrar(stage(), "/vista/Confirmacióndecompra.fxml");
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
    private void regresar() {
        Navegacion.mostrar(stage(), "/vista/Carrito.fxml");
    }

    private Stage stage() {
        return (Stage) lblResultados.getScene().getWindow();
    }

    private void mostrar(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
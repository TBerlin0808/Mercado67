package Controladores;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import mercado67.modelo.ServicioUsuarios;
import mercado67.modelo.Usuario;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
public class InicioDeSesionController implements Initializable {

    @FXML
    private TextField txtCorreo;

    @FXML
    private PasswordField txtContrasena;
    private final ServicioUsuarios servicio = new ServicioUsuarios();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
private void entrar() {
    iniciar(false);
}

@FXML
private void entrarAdmin() {
    iniciar(true);
}

private void iniciar(boolean comoAdmin) {
    String correo = txtCorreo.getText().trim();
    String contrasena = txtContrasena.getText();

    if (correo.isEmpty() || contrasena.isEmpty()) {
        mostrar(Alert.AlertType.WARNING, "Datos incompletos", "Completa el correo y la contrasena.");
        return;
    }
    Usuario u = servicio.iniciarSesion(correo, contrasena);
    if (u == null) {
        mostrar(Alert.AlertType.ERROR, "Error", "Correo o contrasena incorrectos.");
        return;
    }
    if (comoAdmin && !u.isAdmin()) {
        mostrar(Alert.AlertType.WARNING, "Acceso denegado", "Esta cuenta no es de administrador.");
        return;
    }
    abrirPantalla("/vista/Interfaz de usuario.fxml");
}

private void abrirPantalla(String ruta) {
    try {
        Parent root = FXMLLoader.load(getClass().getResource(ruta));
        Stage stage = (Stage) txtCorreo.getScene().getWindow();
        stage.setScene(new Scene(root));
    } catch (Exception e) {
        mostrar(Alert.AlertType.ERROR, "Error", "No se pudo abrir la pantalla.");
    }
}

private void mostrar(Alert.AlertType tipo, String titulo, String mensaje) {
    Alert alerta = new Alert(tipo);
    alerta.setTitle(titulo);
    alerta.setHeaderText(null);
    alerta.setContentText(mensaje);
    alerta.showAndWait();
}
}
package Controladores;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mercado67.modelo.SesionActiva;

public class RegistrateController implements Initializable {

    @FXML
    private TextField txtNombres;

    @FXML
    private TextField txtApellidos;

    @FXML
    private TextField txtCorreo;

    @FXML
    private PasswordField txtContrasena;

    @FXML
    private PasswordField txtRepetir;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    private void crear() {
        String nombre = (txtNombres.getText().trim() + " " + txtApellidos.getText().trim()).trim();
        String correo = txtCorreo.getText().trim();
        String contrasena = txtContrasena.getText();
        String repetir = txtRepetir.getText();

        if (nombre.isEmpty() || correo.isEmpty() || contrasena.isEmpty() || repetir.isEmpty()) {
            mostrar(Alert.AlertType.WARNING, "Datos incompletos", "Completa todos los campos.");
            return;
        }
        if (!contrasena.equals(repetir)) {
            mostrar(Alert.AlertType.WARNING, "Contrasenas distintas", "Las contrasenas no coinciden.");
            return;
        }
        boolean creado = SesionActiva.getServicioUsuarios().registrar(nombre, correo, contrasena);
        if (!creado) {
            mostrar(Alert.AlertType.ERROR, "Error", "Ese correo ya esta registrado.");
            return;
        }
        mostrar(Alert.AlertType.INFORMATION, "Registro", "Cuenta creada. Ya puedes iniciar sesion.");
        abrirPantalla("/vista/Inicio de sesion.fxml");
    }

    @FXML
    private void regresar() {
        abrirPantalla("/vista/Inicio de sesion.fxml");
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
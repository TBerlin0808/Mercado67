package Controladores;
import vista.Navegacion;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import mercado67.modelo.ServicioUsuarios;
import mercado67.modelo.SesionActiva;
import mercado67.modelo.Usuario;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class InicioDeSesionController implements Initializable {

    private static final String CORREO_ADMIN = "admin@mercado67.com";

    @FXML
    private TextField txtCorreo;

    @FXML
    private PasswordField txtContrasena;

    private final ServicioUsuarios servicio = SesionActiva.getServicioUsuarios();

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
            if (servicio.estaDesactivada(correo, contrasena)) {
                mostrar(Alert.AlertType.WARNING, "Cuenta desactivada", "Tu cuenta esta desactivada. Contacta al administrador.");
            } else {
                mostrar(Alert.AlertType.ERROR, "Error", "Correo o contrasena incorrectos.");
            }
            return;
        }
        if (comoAdmin) {
            if (!u.isAdmin() || !u.getCorreo().equalsIgnoreCase(CORREO_ADMIN)) {
                mostrar(Alert.AlertType.WARNING, "Acceso denegado", "Solo la cuenta " + CORREO_ADMIN + " puede entrar al panel de administrador.");
                return;
            }
        } else if (u.isAdmin()) {
            mostrar(Alert.AlertType.WARNING, "Acceso denegado", "Usa el boton Admin para iniciar sesion como administrador.");
            return;
        }

        SesionActiva.setUsuarioActual(u);
        abrirPantalla(comoAdmin ? "/vista/AdminProductos.fxml" : "/vista/Interfaz de usuario.fxml");
    }

    @FXML
    private void irRegistro() {
        abrirPantalla("/vista/Registrate.fxml");
    }

    @FXML
    private void regresar() {
        abrirPantalla("/vista/Inicio.fxml");
    }

    private void abrirPantalla(String ruta) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(ruta));
            Stage stage = (Stage) txtCorreo.getScene().getWindow();
            Navegacion.mostrar(stage, root);
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

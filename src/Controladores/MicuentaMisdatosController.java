/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controladores;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mercado67.modelo.SesionActiva;
import mercado67.modelo.Usuario;
import vista.Navegacion;

/**
 * FXML Controller class
 *
 * @author Miguel
 */
public class MicuentaMisdatosController implements Initializable {

    @FXML
    private Label lblResultados;

    @FXML
    private TextField txtNombres;

    @FXML
    private TextField txtApellidos;

    @FXML
    private TextField txtCorreo;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Usuario usuario = SesionActiva.getUsuarioActual();
        if (usuario == null) {
            return;
        }

        String nombre = (usuario.getNombre() == null) ? "" : usuario.getNombre().trim();
        int espacio = nombre.indexOf(' ');
        if (espacio > 0) {
            txtNombres.setText(nombre.substring(0, espacio));
            txtApellidos.setText(nombre.substring(espacio + 1).trim());
        } else {
            txtNombres.setText(nombre);
            txtApellidos.setText("");
        }
        txtCorreo.setText(usuario.getCorreo());
    }

    @FXML
    private void irMisDatos() {
        Navegacion.mostrar(stage(), "/vista/MicuentaMisdatos.fxml");
    }

    @FXML
    private void irHistorial() {
        Navegacion.mostrar(stage(), "/vista/MicuentaHistorialdecompras.fxml");
    }

    @FXML
    private void irDeseos() {
        Navegacion.mostrar(stage(), "/vista/MicuentaListadedeseos.fxml");
    }

    @FXML
    private void cerrarSesion() {
        if (Navegacion.confirmarCerrarSesion()) {
            SesionActiva.cerrarSesion();
            Navegacion.mostrar(stage(), "/vista/Inicio de sesion.fxml");
        }
    }

    @FXML
    private void irCarrito() {
        Navegacion.mostrar(stage(), "/vista/Carrito.fxml");
    }

    @FXML
    private void regresar() {
        Navegacion.mostrar(stage(), "/vista/Interfaz de usuario.fxml");
    }

    private Stage stage() {
        return (Stage) lblResultados.getScene().getWindow();
    }
}

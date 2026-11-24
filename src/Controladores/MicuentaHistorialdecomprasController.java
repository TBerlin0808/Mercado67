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
import javafx.stage.Stage;
import mercado67.modelo.SesionActiva;
import vista.Navegacion;

/**
 * FXML Controller class
 *
 * @author Miguel
 */
public class MicuentaHistorialdecomprasController implements Initializable {

    @FXML
    private Label lblResultados;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Historial de compras pendiente de mostrar datos.
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
        SesionActiva.cerrarSesion();
        Navegacion.mostrar(stage(), "/vista/Inicio de sesion.fxml");
    }

    @FXML
    private void regresar() {
        Navegacion.mostrar(stage(), "/vista/Interfaz de usuario.fxml");
    }

    private Stage stage() {
        return (Stage) lblResultados.getScene().getWindow();
    }
}

package Controladores;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class InicioController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    private void irLogin(javafx.event.ActionEvent evento) {
        abrirPantalla(evento, "/vista/Inicio de sesion.fxml");
    }

    @FXML
    private void irRegistro(javafx.event.ActionEvent evento) {
        abrirPantalla(evento, "/vista/Registrate.fxml");
    }

    private void abrirPantalla(javafx.event.ActionEvent evento, String ruta) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(ruta));
            Button boton = (Button) evento.getSource();
            Stage stage = (Stage) boton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Error");
            alerta.setHeaderText(null);
            alerta.setContentText("No se pudo abrir la pantalla.");
            alerta.showAndWait();
        }
    }
}
package Controladores;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import mercado67.modelo.ListaEnlazada;
import mercado67.modelo.Producto;
import mercado67.modelo.SesionActiva;

public class ExplorarProductosController implements Initializable {

    @FXML
    private Label lblResultados;

    @FXML
    private Label lblProducto1;

    @FXML
    private Label lblProducto2;

    @FXML
    private Label lblProducto3;

    @FXML
    private Label lblProducto4;

    @FXML
    private Label lblProducto5;

    @FXML
    private Label lblProducto6;

    @FXML
    private Label lblProducto7;

    @FXML
    private Label lblProducto8;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ListaEnlazada<Producto> lista = SesionActiva.getServicioProductos().listarProductos();
        lblResultados.setText(String.valueOf(lista.tamano()));
        mostrar(lblProducto1, lista, 0);
        mostrar(lblProducto2, lista, 1);
        mostrar(lblProducto3, lista, 2);
        mostrar(lblProducto4, lista, 3);
        mostrar(lblProducto5, lista, 4);
        mostrar(lblProducto6, lista, 5);
        mostrar(lblProducto7, lista, 6);
        mostrar(lblProducto8, lista, 7);
    }

    private void mostrar(Label etiqueta, ListaEnlazada<Producto> lista, int posicion) {
        if (posicion < lista.tamano()) {
            Producto p = lista.obtener(posicion);
            etiqueta.setText(p.getNombre() + "\n$" + (int) p.getPrecio());
        }
    }

    @FXML
    private void regresar() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/vista/Interfaz de usuario.fxml"));
            Stage stage = (Stage) lblResultados.getScene().getWindow();
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
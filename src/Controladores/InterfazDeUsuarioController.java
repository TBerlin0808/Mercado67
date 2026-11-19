package Controladores;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import mercado67.modelo.Producto;
import mercado67.modelo.ListaEnlazada;
import mercado67.modelo.ServicioProductos;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import mercado67.modelo.SesionActiva;

public class InterfazDeUsuarioController implements Initializable {

    @FXML
    private Label lblProducto1;

    @FXML
    private Label lblProducto2;

    @FXML
    private Label lblProducto3;

    @FXML
    private Label lblProducto4;

    private final ServicioProductos servicio = SesionActiva.getServicioProductos();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
                ListaEnlazada<Producto> lista = servicio.listarProductos();
        mostrar(lblProducto1, lista, 0);
        mostrar(lblProducto2, lista, 1);
        mostrar(lblProducto3, lista, 2);
        mostrar(lblProducto4, lista, 3);
    }

        private void mostrar(Label etiqueta, ListaEnlazada<Producto> lista, int posicion) {
               if (posicion < lista.tamano()) {
            Producto p = lista.obtener(posicion);
            etiqueta.setText(p.getNombre() + "\n$" + (int) p.getPrecio());
        }
    }
            @FXML
    private void irExplorar() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/vista/Explorar productos.fxml"));
            Stage stage = (Stage) lblProducto1.getScene().getWindow();
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
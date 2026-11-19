package Controladores;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import mercado67.modelo.Producto;
import mercado67.modelo.ListaEnlazada;
import mercado67.modelo.ServicioProductos;

public class InterfazDeUsuarioController implements Initializable {

    @FXML
    private Label lblProducto1;

    @FXML
    private Label lblProducto2;

    @FXML
    private Label lblProducto3;

    @FXML
    private Label lblProducto4;

    private final ServicioProductos servicio = new ServicioProductos();

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
}
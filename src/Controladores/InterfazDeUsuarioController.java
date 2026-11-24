package Controladores;
import vista.Navegacion;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import mercado67.modelo.Producto;
import mercado67.modelo.ServicioProductos;
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

    @FXML
    private ImageView imgProducto1;

    @FXML
    private ImageView imgProducto2;

    @FXML
    private ImageView imgProducto3;

    @FXML
    private ImageView imgProducto4;

    @FXML
    private Pane tarjeta1;

    @FXML
    private Pane tarjeta2;

    @FXML
    private Pane tarjeta3;

    @FXML
    private Pane tarjeta4;

    private final ServicioProductos servicio = SesionActiva.getServicioProductos();
    private final List<Producto> destacados = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Mostrar los productos mas recientes primero.
        for (Producto p : servicio.listarProductos()) {
            destacados.add(p);
        }
        destacados.sort(Comparator.comparingInt(Producto::getIdProducto).reversed());

        mostrarTarjeta(tarjeta1, lblProducto1, imgProducto1, 0);
        mostrarTarjeta(tarjeta2, lblProducto2, imgProducto2, 1);
        mostrarTarjeta(tarjeta3, lblProducto3, imgProducto3, 2);
        mostrarTarjeta(tarjeta4, lblProducto4, imgProducto4, 3);
    }

    private void mostrarTarjeta(Pane tarjeta, Label etiqueta, ImageView imagen, int posicion) {
        if (posicion < destacados.size()) {
            Producto p = destacados.get(posicion);
            etiqueta.setText(p.getNombre() + "\n$" + (int) p.getPrecio());
            imagen.setImage(cargarFoto(p));
            tarjeta.setVisible(true);
            tarjeta.setManaged(true);
        } else {
            etiqueta.setText("");
            imagen.setImage(null);
            tarjeta.setVisible(false);
            tarjeta.setManaged(false);
        }
    }

    private Image cargarFoto(Producto p) {
        if (p.getRutasFotos() == null || p.getRutasFotos().isEmpty()) {
            return null;
        }
        File archivo = new File(p.getRutasFotos().get(0));
        if (!archivo.exists()) {
            return null;
        }
        Image imagen = new Image(archivo.toURI().toString());
        return imagen.isError() ? null : imagen;
    }

    @FXML
    private void agregar1() {
        agregarAlCarrito(0);
    }

    @FXML
    private void agregar2() {
        agregarAlCarrito(1);
    }

    @FXML
    private void agregar3() {
        agregarAlCarrito(2);
    }

    @FXML
    private void agregar4() {
        agregarAlCarrito(3);
    }

    private void agregarAlCarrito(int posicion) {
        if (posicion >= destacados.size()) {
            return;
        }
        Producto p = destacados.get(posicion);
        SesionActiva.getCarrito().agregar(p);
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Carrito");
        alerta.setHeaderText(null);
        alerta.setContentText(p.getNombre() + " agregado al carrito. Total: $" + (int) SesionActiva.getCarrito().getTotal());
        alerta.showAndWait();
    }

    @FXML
    private void irMiCuenta() {
        Navegacion.mostrar((Stage) lblProducto1.getScene().getWindow(), "/vista/MicuentaMisdatos.fxml");
    }

    @FXML
    private void irExplorar() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/vista/Explorar productos.fxml"));
            Stage stage = (Stage) lblProducto1.getScene().getWindow();
            Navegacion.mostrar(stage, root);
        } catch (Exception e) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Error");
            alerta.setHeaderText(null);
            alerta.setContentText("No se pudo abrir la pantalla.");
            alerta.showAndWait();
        }
    }

    @FXML
    private void irPublicar() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/vista/Publicar producto.fxml"));
            Stage stage = (Stage) lblProducto1.getScene().getWindow();
            Navegacion.mostrar(stage, root);
        } catch (Exception e) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Error");
            alerta.setHeaderText(null);
            alerta.setContentText("No se pudo abrir la pantalla de publicar producto.");
            alerta.showAndWait();
        }
    }
}

package Controladores;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import mercado67.modelo.Producto;
import mercado67.modelo.SesionActiva;
import vista.Navegacion;

public class AdminEditarproductoController implements Initializable {

    @FXML private TextField txtNombre;
    @FXML private TextField txtPrecio;
    @FXML private TextField txtCantidad;
    @FXML private TextField txtDescripcion;
    @FXML private ImageView imgFoto;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Producto p = SesionActiva.getProductoSeleccionado();
        if (p == null) {
            return;
        }
        txtNombre.setText(p.getNombre());
        txtPrecio.setText(String.valueOf((int) p.getPrecio()));
        txtCantidad.setText(String.valueOf(p.getCantidad()));
        txtDescripcion.setText(p.getDescripcion());
        if (p.getRutasFotos() != null && !p.getRutasFotos().isEmpty()) {
            File archivo = new File(p.getRutasFotos().get(0));
            if (archivo.exists()) {
                Image imagen = new Image(archivo.toURI().toString());
                if (!imagen.isError()) {
                    imgFoto.setImage(imagen);
                }
            }
        }
    }

    @FXML
    private void guardar() {
        Producto p = SesionActiva.getProductoSeleccionado();
        if (p == null) {
            return;
        }
        String nombre = txtNombre.getText().trim();
        String descripcion = txtDescripcion.getText().trim();
        if (nombre.isEmpty() || descripcion.isEmpty()) {
            mostrar(Alert.AlertType.WARNING, "Datos incompletos", "Completa nombre y descripcion.");
            return;
        }
        double precio;
        int cantidad;
        try {
            precio = Double.parseDouble(txtPrecio.getText().trim());
            cantidad = Integer.parseInt(txtCantidad.getText().trim());
        } catch (NumberFormatException e) {
            mostrar(Alert.AlertType.ERROR, "Error", "Precio y stock deben ser numeros.");
            return;
        }
        if (precio <= 0 || cantidad < 0) {
            mostrar(Alert.AlertType.ERROR, "Error", "Precio mayor a 0 y stock no negativo.");
            return;
        }
        p.setNombre(nombre);
        p.setPrecio(precio);
        p.setCantidad(cantidad);
        p.setDescripcion(descripcion);
        SesionActiva.getServicioProductos().guardarCambios();
        mostrar(Alert.AlertType.INFORMATION, "Producto", "Cambios guardados.");
        volver();
    }

    @FXML
    private void cancelar() {
        volver();
    }

    private void volver() {
        SesionActiva.setProductoSeleccionado(null);
        Navegacion.mostrar((Stage) txtNombre.getScene().getWindow(), "/vista/AdminProductos.fxml");
    }

    private void mostrar(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
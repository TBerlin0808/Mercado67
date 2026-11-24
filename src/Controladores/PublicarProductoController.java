/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controladores;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import mercado67.modelo.Producto;
import mercado67.modelo.SesionActiva;
import mercado67.modelo.Usuario;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class PublicarProductoController implements Initializable {

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtPrecio;

    @FXML
    private TextField txtCantidad;

    @FXML
    private TextArea txtDescripcion;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void publicarProducto() {
        String nombre = txtNombre.getText().trim();
        String precioTexto = txtPrecio.getText().trim();
        String cantidadTexto = txtCantidad.getText().trim();
        String descripcion = txtDescripcion.getText().trim();

        if (nombre.isEmpty() || precioTexto.isEmpty() || cantidadTexto.isEmpty() || descripcion.isEmpty()) {
            mostrar(Alert.AlertType.WARNING, "Datos incompletos", "Por favor completa todos los campos.");
            return;
        }

        double precio;
        int cantidad;
        try {
            precio = Double.parseDouble(precioTexto);
            cantidad = Integer.parseInt(cantidadTexto);
        } catch (NumberFormatException e) {
            mostrar(Alert.AlertType.ERROR, "Error de formato", "Precio y cantidad deben ser numeros validos.");
            return;
        }

        if (precio <= 0 || cantidad <= 0) {
            mostrar(Alert.AlertType.ERROR, "Error de valores", "Precio y cantidad deben ser mayores a cero.");
            return;
        }

        Usuario vendedor = SesionActiva.getUsuarioActual();
        int idVendedor = (vendedor != null) ? vendedor.getNombre().hashCode() : 0;

        Producto producto = new Producto(0, nombre, precio, cantidad, descripcion, idVendedor);
        SesionActiva.getServicioProductos().agregarProducto(producto);

        mostrar(Alert.AlertType.INFORMATION, "Producto publicado", "Tu producto se ha publicado correctamente.");
        limpiarCampos();
        regresarAlInicio();
    }

    @FXML
    private void cancelar() {
        regresarAlInicio();
    }

    private void limpiarCampos() {
        txtNombre.clear();
        txtPrecio.clear();
        txtCantidad.clear();
        txtDescripcion.clear();
    }

    private void regresarAlInicio() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/vista/Interfaz de usuario.fxml"));
            Stage stage = (Stage) txtNombre.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            mostrar(Alert.AlertType.ERROR, "Error", "No se pudo regresar a la pantalla principal.");
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

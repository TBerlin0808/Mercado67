/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controladores;
import vista.Navegacion;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import mercado67.modelo.Producto;
import mercado67.modelo.SesionActiva;
import mercado67.modelo.Usuario;

public class PublicarProductoController implements Initializable {

    private static final String CARPETA_FOTOS = "data/fotos";

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtPrecio;

    @FXML
    private TextField txtCantidad;

    @FXML
    private TextArea txtDescripcion;

    @FXML
    private Button btnFoto1;

    @FXML
    private Button btnFoto2;

    @FXML
    private Button btnFoto3;

    @FXML
    private ImageView imgFoto1;

    @FXML
    private ImageView imgFoto2;

    @FXML
    private ImageView imgFoto3;

    @FXML
    private Label lblFoto1;

    @FXML
    private Label lblFoto2;

    @FXML
    private Label lblFoto3;

    private File archivoFoto1;
    private File archivoFoto2;
    private File archivoFoto3;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    /* ---------- Seleccion de fotos ---------- */

    @FXML
    private void seleccionarFoto1() {
        archivoFoto1 = seleccionarImagen(btnFoto1, imgFoto1, lblFoto1, archivoFoto1);
    }

    @FXML
    private void seleccionarFoto2() {
        archivoFoto2 = seleccionarImagen(btnFoto2, imgFoto2, lblFoto2, archivoFoto2);
    }

    @FXML
    private void seleccionarFoto3() {
        archivoFoto3 = seleccionarImagen(btnFoto3, imgFoto3, lblFoto3, archivoFoto3);
    }

    private File seleccionarImagen(Button boton, ImageView vista, Label etiqueta, File actual) {
        FileChooser selector = new FileChooser();
        selector.setTitle("Seleccionar imagen del producto");
        selector.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imagenes", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp"),
                new FileChooser.ExtensionFilter("Todos los archivos", "*.*")
        );
        if (actual != null && actual.getParentFile() != null) {
            selector.setInitialDirectory(actual.getParentFile());
        }

        Stage ventana = (Stage) boton.getScene().getWindow();
        File archivo = selector.showOpenDialog(ventana);
        if (archivo == null) {
            return actual;
        }

        Image imagen = new Image(archivo.toURI().toString());
        if (imagen.isError()) {
            mostrar(Alert.AlertType.ERROR, "Imagen invalida",
                    "No se pudo leer el archivo seleccionado. Intenta con otra imagen.");
            return actual;
        }

        vista.setImage(imagen);
        vista.setVisible(true);
        if (etiqueta != null) {
            etiqueta.setVisible(false);
        }
        boton.setText("");
        return archivo;
    }

    /* ---------- Publicar ---------- */

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

        if (archivoFoto1 == null) {
            mostrar(Alert.AlertType.WARNING, "Falta la portada",
                    "Debes subir al menos la foto de portada del producto.");
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

        int fotosGuardadas = 0;
        fotosGuardadas += guardarFoto(producto, archivoFoto1);
        fotosGuardadas += guardarFoto(producto, archivoFoto2);
        fotosGuardadas += guardarFoto(producto, archivoFoto3);

        if (fotosGuardadas == 0) {
            mostrar(Alert.AlertType.ERROR, "Error al guardar",
                    "No se pudieron guardar las fotos del producto. Intenta de nuevo.");
            return;
        }

        SesionActiva.getServicioProductos().agregarProducto(producto);

        mostrar(Alert.AlertType.INFORMATION, "Producto publicado",
                "Tu producto se ha publicado correctamente con " + fotosGuardadas + " foto(s).");
        limpiarCampos();
        regresarAlInicio();
    }

    /**
     * Copia el archivo de imagen a data/fotos con un nombre unico y agrega
     * la ruta relativa al producto. Retorna 1 si se guardo, 0 si no.
     */
    private int guardarFoto(Producto producto, File original) {
        if (original == null) {
            return 0;
        }
        try {
            Path destinoDir = Paths.get(CARPETA_FOTOS);
            if (!Files.exists(destinoDir)) {
                Files.createDirectories(destinoDir);
            }

            String extension = obtenerExtension(original.getName());
            String nombreDestino = System.nanoTime() + "_"
                    + Math.abs(original.getName().hashCode()) + extension;
            Path destino = destinoDir.resolve(nombreDestino);

            Files.copy(original.toPath(), destino, StandardCopyOption.REPLACE_EXISTING);
            producto.agregarFoto(destino.toString().replace('\\', '/'));
            return 1;
        } catch (IOException e) {
            return 0;
        }
    }

    private String obtenerExtension(String nombre) {
        int punto = nombre.lastIndexOf('.');
        if (punto >= 0) {
            return nombre.substring(punto).toLowerCase();
        }
        return ".jpg";
    }

    @FXML
    private void irMiCuenta() {
        Navegacion.mostrar((Stage) txtNombre.getScene().getWindow(), "/vista/MicuentaMisdatos.fxml");
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
        archivoFoto1 = null;
        archivoFoto2 = null;
        archivoFoto3 = null;
        imgFoto1.setImage(null);
        imgFoto2.setImage(null);
        imgFoto3.setImage(null);
        lblFoto1.setVisible(true);
        lblFoto2.setVisible(true);
        lblFoto3.setVisible(true);
        btnFoto1.setText("Subir foto Portada");
        btnFoto2.setText("Foto 2");
        btnFoto3.setText("Foto 3");
    }

    private void regresarAlInicio() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/vista/Interfaz de usuario.fxml"));
            Stage stage = (Stage) txtNombre.getScene().getWindow();
            Navegacion.mostrar(stage, root);
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

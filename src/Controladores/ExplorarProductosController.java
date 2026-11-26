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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import mercado67.modelo.Producto;
import mercado67.modelo.SesionActiva;

public class ExplorarProductosController implements Initializable {

    private static final int PRODUCTOS_POR_PAGINA = 8;

    @FXML
    private TextField txtBuscar;

    @FXML
    private Label lblResultados;

    @FXML
    private Label lblPagina;

    @FXML
    private Button btnAnterior;

    @FXML
    private Button btnSiguiente;

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

    @FXML
    private ImageView imgProducto1;

    @FXML
    private ImageView imgProducto2;

    @FXML
    private ImageView imgProducto3;

    @FXML
    private ImageView imgProducto4;

    @FXML
    private ImageView imgProducto5;

    @FXML
    private ImageView imgProducto6;

    @FXML
    private ImageView imgProducto7;

    @FXML
    private ImageView imgProducto8;

    private final Producto[] productosPagina = new Producto[PRODUCTOS_POR_PAGINA];
    private int paginaActual = 1;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txtBuscar.textProperty().addListener((observable, anterior, nuevo) -> {
            paginaActual = 1;
            actualizarVista();
        });
        actualizarVista();
    }

    /**
     * Obtiene todos los productos, los filtra por el texto de busqueda y los
     * ordena del mas nuevo al mas antiguo (id descendente).
     */
    private List<Producto> obtenerProductosFiltrados() {
        List<Producto> lista = new ArrayList<>();
        for (Producto p : SesionActiva.getServicioProductos().listarProductos()) {
            lista.add(p);
        }

        String filtro = txtBuscar.getText();
        filtro = (filtro == null) ? "" : filtro.trim().toLowerCase();

        if (!filtro.isEmpty()) {
            List<Producto> filtrados = new ArrayList<>();
            for (Producto p : lista) {
                String nombre = (p.getNombre() == null) ? "" : p.getNombre().toLowerCase();
                String descripcion = (p.getDescripcion() == null) ? "" : p.getDescripcion().toLowerCase();
                if (nombre.contains(filtro) || descripcion.contains(filtro)) {
                    filtrados.add(p);
                }
            }
            lista = filtrados;
        }

        lista.sort(Comparator.comparingInt(Producto::getIdProducto).reversed());
        return lista;
    }

    private void actualizarVista() {
        List<Producto> filtrados = obtenerProductosFiltrados();
        int total = filtrados.size();
        // Siempre se permite una pagina adicional, aunque quede vacia, para
        // que las flechas sigan funcionando cuando hay pocos productos.
        int totalPaginas = Math.max(2,
                (total + PRODUCTOS_POR_PAGINA - 1) / PRODUCTOS_POR_PAGINA + 1);

        if (paginaActual > totalPaginas) {
            paginaActual = totalPaginas;
        }
        if (paginaActual < 1) {
            paginaActual = 1;
        }

        Label[] etiquetas = {lblProducto1, lblProducto2, lblProducto3, lblProducto4,
            lblProducto5, lblProducto6, lblProducto7, lblProducto8};
        ImageView[] imagenes = {imgProducto1, imgProducto2, imgProducto3, imgProducto4,
            imgProducto5, imgProducto6, imgProducto7, imgProducto8};

        for (int i = 0; i < PRODUCTOS_POR_PAGINA; i++) {
            etiquetas[i].setText("");
            imagenes[i].setImage(null);
            productosPagina[i] = null;
        }

        int inicio = (paginaActual - 1) * PRODUCTOS_POR_PAGINA;
        for (int i = 0; i < PRODUCTOS_POR_PAGINA; i++) {
            int indice = inicio + i;
            if (indice >= total) {
                break;
            }
            Producto p = filtrados.get(indice);
            productosPagina[i] = p;
            etiquetas[i].setText(p.getNombre() + "\n$" + (int) p.getPrecio());
            cargarImagen(imagenes[i], p);
        }

        lblResultados.setText(String.valueOf(total));
        lblPagina.setText(paginaActual + " / " + totalPaginas);
        btnAnterior.setDisable(paginaActual <= 1);
        btnSiguiente.setDisable(paginaActual >= totalPaginas);
    }

    private void cargarImagen(ImageView vista, Producto producto) {
        if (producto.getRutasFotos() == null || producto.getRutasFotos().isEmpty()) {
            return;
        }
        File archivo = new File(producto.getRutasFotos().get(0));
        if (!archivo.exists()) {
            return;
        }
        Image imagen = new Image(archivo.toURI().toString());
        if (!imagen.isError()) {
            vista.setImage(imagen);
        }
    }

    @FXML
    private void paginaAnterior() {
        paginaActual--;
        actualizarVista();
    }

    @FXML
    private void paginaSiguiente() {
        paginaActual++;
        actualizarVista();
    }

    @FXML
    private void abrirProducto1() {
        abrirProducto(0);
    }

    @FXML
    private void abrirProducto2() {
        abrirProducto(1);
    }

    @FXML
    private void abrirProducto3() {
        abrirProducto(2);
    }

    @FXML
    private void abrirProducto4() {
        abrirProducto(3);
    }

    @FXML
    private void abrirProducto5() {
        abrirProducto(4);
    }

    @FXML
    private void abrirProducto6() {
        abrirProducto(5);
    }

    @FXML
    private void abrirProducto7() {
        abrirProducto(6);
    }

    @FXML
    private void abrirProducto8() {
        abrirProducto(7);
    }

    private void abrirProducto(int indice) {
        Producto producto = productosPagina[indice];
        if (producto == null) {
            return;
        }
        SesionActiva.setProductoSeleccionado(producto);
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/vista/Detalledeproducto.fxml"));
            Stage stage = (Stage) lblResultados.getScene().getWindow();
            Navegacion.mostrar(stage, root);
        } catch (Exception e) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Error");
            alerta.setHeaderText(null);
            alerta.setContentText("No se pudo abrir el detalle del producto.");
            alerta.showAndWait();
        }
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

    @FXML
    private void agregar5() {
        agregarAlCarrito(4);
    }

    @FXML
    private void agregar6() {
        agregarAlCarrito(5);
    }

    @FXML
    private void agregar7() {
        agregarAlCarrito(6);
    }

    @FXML
    private void agregar8() {
        agregarAlCarrito(7);
    }

    private void agregarAlCarrito(int indice) {
        Producto producto = productosPagina[indice];
        if (producto == null) {
            return;
        }
        SesionActiva.getCarrito().agregar(producto);
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Carrito");
        alerta.setHeaderText(null);
        alerta.setContentText(producto.getNombre() + " agregado al carrito. Total: $"
                + (int) SesionActiva.getCarrito().getTotal());
        alerta.showAndWait();
    }

    @FXML
    private void irMiCuenta() {
        Navegacion.mostrar((Stage) lblResultados.getScene().getWindow(), "/vista/MicuentaMisdatos.fxml");
    }

    @FXML
    private void regresar() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/vista/Interfaz de usuario.fxml"));
            Stage stage = (Stage) lblResultados.getScene().getWindow();
            Navegacion.mostrar(stage, root);
        } catch (Exception e) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Error");
            alerta.setHeaderText(null);
            alerta.setContentText("No se pudo abrir la pantalla.");
            alerta.showAndWait();
        }
    }
}

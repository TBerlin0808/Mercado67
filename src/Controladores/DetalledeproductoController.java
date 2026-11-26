/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import mercado67.modelo.Producto;
import mercado67.modelo.SesionActiva;
import mercado67.modelo.Usuario;

/**
 * FXML Controller class
 *
 * @author Miguel
 */
public class DetalledeproductoController implements Initializable {

    @FXML
    private ImageView imgProducto;

    @FXML
    private ImageView imgMiniatura1;

    @FXML
    private ImageView imgMiniatura2;

    @FXML
    private ImageView imgMiniatura3;

    @FXML
    private StackPane paneMiniatura1;

    @FXML
    private StackPane paneMiniatura2;

    @FXML
    private StackPane paneMiniatura3;

    @FXML
    private Label lblNombre;

    @FXML
    private Label lblPrecio;

    @FXML
    private Label lblVendedor;

    @FXML
    private Label lblDescripcion;

    @FXML
    private Label lblCantidad;

    @FXML
    private Label lblPaginaProducto;

    @FXML
    private Button btnAnterior;

    @FXML
    private Button btnSiguiente;

    @FXML
    private Button btnAgregarCarrito;

    @FXML
    private Button btnMenos;

    @FXML
    private Button btnMas;

    private final List<Producto> productos = new ArrayList<>();
    private int indiceActual;
    private int cantidad = 1;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Cargar la lista completa ordenada de la misma forma que Explorar.
        for (Producto p : SesionActiva.getServicioProductos().listarProductos()) {
            productos.add(p);
        }
        productos.sort(Comparator.comparingInt(Producto::getIdProducto).reversed());

        Producto seleccionado = SesionActiva.getProductoSeleccionado();
        indiceActual = 0;
        if (seleccionado != null) {
            for (int i = 0; i < productos.size(); i++) {
                if (productos.get(i).getIdProducto() == seleccionado.getIdProducto()) {
                    indiceActual = i;
                    break;
                }
            }
        }

        if (paneMiniatura1 != null) {
            paneMiniatura1.setOnMouseClicked(e -> mostrarFoto(0));
        }
        if (paneMiniatura2 != null) {
            paneMiniatura2.setOnMouseClicked(e -> mostrarFoto(1));
        }
        if (paneMiniatura3 != null) {
            paneMiniatura3.setOnMouseClicked(e -> mostrarFoto(2));
        }

        mostrarProducto();
    }

    private void mostrarProducto() {
        if (productos.isEmpty()) {
            lblNombre.setText("No hay productos disponibles");
            lblPrecio.setText("");
            lblVendedor.setText("");
            lblDescripcion.setText("");
            imgProducto.setImage(null);
            ocultarMiniatura(paneMiniatura1);
            ocultarMiniatura(paneMiniatura2);
            ocultarMiniatura(paneMiniatura3);
            lblPaginaProducto.setText("0 / 0");
            btnAnterior.setDisable(true);
            btnSiguiente.setDisable(true);
            cantidad = 0;
            lblCantidad.setText("0");
            btnAgregarCarrito.setDisable(true);
            btnMenos.setDisable(true);
            btnMas.setDisable(true);
            return;
        }

        if (indiceActual < 0) {
            indiceActual = 0;
        }
        if (indiceActual >= productos.size()) {
            indiceActual = productos.size() - 1;
        }

        Producto p = productos.get(indiceActual);
        SesionActiva.setProductoSeleccionado(p);

        boolean agotado = p.getCantidad() <= 0;
        int disponible = disponible(p);

        lblNombre.setText(p.getNombre() + (agotado ? " (Agotado)" : ""));
        lblPrecio.setText("$" + (int) p.getPrecio());
        lblVendedor.setText(obtenerNombreVendedor(p));
        lblDescripcion.setText(p.getDescripcion());

        cantidad = disponible > 0 ? 1 : 0;
        lblCantidad.setText(String.valueOf(cantidad));
        btnAgregarCarrito.setDisable(disponible <= 0);
        btnMenos.setDisable(cantidad <= 1);
        btnMas.setDisable(cantidad >= disponible);

        imgProducto.setImage(obtenerImagen(p, 0));
        cargarMiniaturas(p);

        lblPaginaProducto.setText((indiceActual + 1) + " / " + productos.size());
        btnAnterior.setDisable(indiceActual == 0);
        btnSiguiente.setDisable(indiceActual == productos.size() - 1);
    }

    private void cargarMiniaturas(Producto p) {
        cargarMiniatura(paneMiniatura1, imgMiniatura1, p, 0);
        cargarMiniatura(paneMiniatura2, imgMiniatura2, p, 1);
        cargarMiniatura(paneMiniatura3, imgMiniatura3, p, 2);
    }

    private void cargarMiniatura(StackPane contenedor, ImageView vista, Producto p, int indiceFoto) {
        Image imagen = obtenerImagen(p, indiceFoto);
        vista.setImage(imagen);
        if (imagen == null) {
            ocultarMiniatura(contenedor);
        } else {
            contenedor.setVisible(true);
            contenedor.setManaged(true);
        }
    }

    private void ocultarMiniatura(StackPane contenedor) {
        if (contenedor != null) {
            contenedor.setVisible(false);
            contenedor.setManaged(false);
        }
    }

    private void mostrarFoto(int indiceFoto) {
        Producto p = productoActual();
        if (p == null) {
            return;
        }
        Image imagen = obtenerImagen(p, indiceFoto);
        if (imagen != null) {
            imgProducto.setImage(imagen);
        }
    }

    private Image obtenerImagen(Producto p, int indiceFoto) {
        if (p == null || p.getRutasFotos() == null || indiceFoto >= p.getRutasFotos().size()) {
            return null;
        }
        File archivo = new File(p.getRutasFotos().get(indiceFoto));
        if (!archivo.exists()) {
            return null;
        }
        Image imagen = new Image(archivo.toURI().toString());
        return imagen.isError() ? null : imagen;
    }

    private String obtenerNombreVendedor(Producto p) {
        for (Usuario u : SesionActiva.getServicioUsuarios().obtenerTodos()) {
            if (u.getNombre() != null && u.getNombre().hashCode() == p.getIdVendedor()) {
                return u.getNombre();
            }
        }
        return "Vendedor #" + p.getIdVendedor();
    }

    private Producto productoActual() {
        if (productos.isEmpty() || indiceActual < 0 || indiceActual >= productos.size()) {
            return null;
        }
        return productos.get(indiceActual);
    }

    @FXML
    private void productoAnterior() {
        if (indiceActual > 0) {
            indiceActual--;
            mostrarProducto();
        }
    }

    @FXML
    private void productoSiguiente() {
        if (indiceActual < productos.size() - 1) {
            indiceActual++;
            mostrarProducto();
        }
    }

    private int disponible(Producto p) {
        if (p == null) {
            return 0;
        }
        return Math.max(0, p.getCantidad() - SesionActiva.getCarrito().cantidadDe(p.getIdProducto()));
    }

    @FXML
    private void aumentarCantidad() {
        Producto p = productoActual();
        if (p == null) {
            return;
        }
        int disponible = disponible(p);
        if (cantidad < disponible) {
            cantidad++;
            lblCantidad.setText(String.valueOf(cantidad));
            btnMenos.setDisable(cantidad <= 1);
            btnMas.setDisable(cantidad >= disponible);
        }
    }

    @FXML
    private void disminuirCantidad() {
        if (cantidad > 1) {
            cantidad--;
            lblCantidad.setText(String.valueOf(cantidad));
            btnMenos.setDisable(cantidad <= 1);
            btnMas.setDisable(cantidad >= disponible(productoActual()));
        }
    }

    @FXML
    private void agregarAlCarrito() {
        Producto p = productoActual();
        if (p == null) {
            return;
        }
        int disponible = disponible(p);
        if (cantidad <= 0 || cantidad > disponible) {
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setTitle("Stock insuficiente");
            alerta.setHeaderText(null);
            alerta.setContentText("No hay suficiente stock de " + p.getNombre()
                    + ". Disponible: " + disponible + ".");
            alerta.showAndWait();
            mostrarProducto();
            return;
        }
        boolean agregado = SesionActiva.getCarrito().agregar(p, cantidad);
        if (!agregado) {
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setTitle("Stock insuficiente");
            alerta.setHeaderText(null);
            alerta.setContentText("No hay mas unidades disponibles de " + p.getNombre() + ".");
            alerta.showAndWait();
            mostrarProducto();
            return;
        }
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Carrito");
        alerta.setHeaderText(null);
        alerta.setContentText(cantidad + " x " + p.getNombre()
                + " agregado(s) al carrito. Total: $" + (int) SesionActiva.getCarrito().getTotal());
        alerta.showAndWait();
        mostrarProducto();
    }

    @FXML
    private void agregarADeseos() {
        Producto p = productoActual();
        if (p == null) {
            return;
        }
        Usuario usuario = SesionActiva.getUsuarioActual();
        if (usuario == null) {
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setTitle("Lista de deseos");
            alerta.setHeaderText(null);
            alerta.setContentText("Debes iniciar sesion para guardar productos en tu lista de deseos.");
            alerta.showAndWait();
            return;
        }

        boolean agregado = SesionActiva.getServicioDeseos().agregar(usuario.getCorreo(), p.getIdProducto());
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Lista de deseos");
        alerta.setHeaderText(null);
        alerta.setContentText(agregado
                ? p.getNombre() + " se agrego a tu lista de deseos."
                : p.getNombre() + " ya esta en tu lista de deseos.");
        alerta.showAndWait();
    }

    @FXML
    private void irCarrito() {
        Navegacion.mostrar((Stage) lblNombre.getScene().getWindow(), "/vista/Carrito.fxml");
    }

    @FXML
    private void irMiCuenta() {
        Navegacion.mostrar((Stage) lblNombre.getScene().getWindow(), "/vista/MicuentaMisdatos.fxml");
    }

    @FXML
    private void regresar() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/vista/Explorar productos.fxml"));
            Stage stage = (Stage) lblNombre.getScene().getWindow();
            Navegacion.mostrar(stage, root);
        } catch (Exception e) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Error");
            alerta.setHeaderText(null);
            alerta.setContentText("No se pudo regresar a la pantalla de productos.");
            alerta.showAndWait();
        }
    }
}

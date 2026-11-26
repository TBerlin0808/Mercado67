/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controladores;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import mercado67.modelo.Producto;
import mercado67.modelo.SesionActiva;
import mercado67.modelo.Usuario;
import vista.Navegacion;

/**
 * FXML Controller class
 *
 * @author Miguel
 */
public class MicuentaListadedeseosController implements Initializable {

    private static final int DESEOS_POR_PAGINA = 6;

    @FXML
    private Label lblResultados;

    @FXML
    private Label lblPaginaPedidos;

    @FXML
    private Label lblPaginaPedidos1;

    @FXML
    private Button btnAnteriorPedidos;

    @FXML
    private Button btnSiguientePedidos;

    @FXML
    private Label lblDeseo1;

    @FXML
    private Label lblDeseo2;

    @FXML
    private Label lblDeseo3;

    @FXML
    private Label lblDeseo4;

    @FXML
    private Label lblDeseo5;

    @FXML
    private Label lblDeseo6;

    @FXML
    private ImageView imgDeseo1;

    @FXML
    private ImageView imgDeseo2;

    @FXML
    private ImageView imgDeseo3;

    @FXML
    private ImageView imgDeseo4;

    @FXML
    private ImageView imgDeseo5;

    @FXML
    private ImageView imgDeseo6;

    @FXML
    private Pane tarjetaDeseo1;

    @FXML
    private Pane tarjetaDeseo2;

    @FXML
    private Pane tarjetaDeseo3;

    @FXML
    private Pane tarjetaDeseo4;

    @FXML
    private Pane tarjetaDeseo5;

    @FXML
    private Pane tarjetaDeseo6;

    private final List<Producto> deseos = new ArrayList<>();
    private int pagina = 0;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDeseos();
    }

    private void cargarDeseos() {
        deseos.clear();
        Usuario usuario = SesionActiva.getUsuarioActual();
        if (usuario != null) {
            for (Integer id : SesionActiva.getServicioDeseos().obtenerIds(usuario.getCorreo())) {
                Producto p = SesionActiva.getServicioProductos().buscarPorId(id);
                if (p != null) {
                    deseos.add(p);
                }
            }
        }
        actualizarVista();
    }

    private void actualizarVista() {
        Label[] etiquetas = {lblDeseo1, lblDeseo2, lblDeseo3, lblDeseo4, lblDeseo5, lblDeseo6};
        ImageView[] imagenes = {imgDeseo1, imgDeseo2, imgDeseo3, imgDeseo4, imgDeseo5, imgDeseo6};
        Pane[] tarjetas = {tarjetaDeseo1, tarjetaDeseo2, tarjetaDeseo3, tarjetaDeseo4, tarjetaDeseo5, tarjetaDeseo6};

        int total = deseos.size();
        int totalPaginas = Math.max(1, (total + DESEOS_POR_PAGINA - 1) / DESEOS_POR_PAGINA);
        if (pagina >= totalPaginas) {
            pagina = totalPaginas - 1;
        }
        if (pagina < 0) {
            pagina = 0;
        }

        for (int i = 0; i < etiquetas.length; i++) {
            etiquetas[i].setText("");
            imagenes[i].setImage(null);
            tarjetas[i].setVisible(false);
            tarjetas[i].setManaged(false);
        }

        int inicio = pagina * DESEOS_POR_PAGINA;
        for (int i = 0; i < etiquetas.length; i++) {
            int indice = inicio + i;
            if (indice >= total) {
                break;
            }
            Producto p = deseos.get(indice);
            String agotado = p.getCantidad() <= 0 ? " (Agotado)" : "";
            etiquetas[i].setText(p.getNombre() + agotado + "\n$" + (int) p.getPrecio());
            imagenes[i].setImage(cargarFoto(p));
            tarjetas[i].setVisible(true);
            tarjetas[i].setManaged(true);
        }

        lblResultados.setText(String.valueOf(total));
        lblPaginaPedidos.setText(String.valueOf(pagina + 1));
        lblPaginaPedidos1.setText(String.valueOf(totalPaginas));
        btnAnteriorPedidos.setDisable(pagina == 0);
        btnSiguientePedidos.setDisable(pagina >= totalPaginas - 1);
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

    @FXML
    private void agregar5() {
        agregarAlCarrito(4);
    }

    @FXML
    private void agregar6() {
        agregarAlCarrito(5);
    }

    private void agregarAlCarrito(int posicion) {
        int indice = pagina * DESEOS_POR_PAGINA + posicion;
        if (indice >= deseos.size()) {
            return;
        }
        Producto p = deseos.get(indice);
        if (!SesionActiva.getCarrito().agregar(p)) {
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setTitle("Sin stock");
            alerta.setHeaderText(null);
            alerta.setContentText("No hay mas unidades disponibles de " + p.getNombre() + ".");
            alerta.showAndWait();
            return;
        }
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Carrito");
        alerta.setHeaderText(null);
        alerta.setContentText(p.getNombre() + " agregado al carrito. Total: $"
                + (int) SesionActiva.getCarrito().getTotal());
        alerta.showAndWait();
    }

    @FXML
    private void quitar1() {
        quitarDeseo(0);
    }

    @FXML
    private void quitar2() {
        quitarDeseo(1);
    }

    @FXML
    private void quitar3() {
        quitarDeseo(2);
    }

    @FXML
    private void quitar4() {
        quitarDeseo(3);
    }

    @FXML
    private void quitar5() {
        quitarDeseo(4);
    }

    @FXML
    private void quitar6() {
        quitarDeseo(5);
    }

    private void quitarDeseo(int posicion) {
        int indice = pagina * DESEOS_POR_PAGINA + posicion;
        if (indice >= deseos.size()) {
            return;
        }
        Producto p = deseos.get(indice);
        Usuario usuario = SesionActiva.getUsuarioActual();
        if (usuario != null) {
            SesionActiva.getServicioDeseos().quitar(usuario.getCorreo(), p.getIdProducto());
        }
        cargarDeseos();
    }

    private void abrirDetalle(int posicionVista) {
        int posicion = pagina * DESEOS_POR_PAGINA + posicionVista;
        if (posicion >= deseos.size()) {
            return;
        }
        SesionActiva.setProductoSeleccionado(deseos.get(posicion));
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

    @FXML private void abrirDetalle1() { abrirDetalle(0); }
    @FXML private void abrirDetalle2() { abrirDetalle(1); }
    @FXML private void abrirDetalle3() { abrirDetalle(2); }
    @FXML private void abrirDetalle4() { abrirDetalle(3); }
    @FXML private void abrirDetalle5() { abrirDetalle(4); }
    @FXML private void abrirDetalle6() { abrirDetalle(5); }

    @FXML
    private void paginaAnterior() {
        if (pagina > 0) {
            pagina--;
            actualizarVista();
        }
    }

    @FXML
    private void paginaSiguiente() {
        int totalPaginas = Math.max(1, (deseos.size() + DESEOS_POR_PAGINA - 1) / DESEOS_POR_PAGINA);
        if (pagina < totalPaginas - 1) {
            pagina++;
            actualizarVista();
        }
    }

    @FXML
    private void irMisDatos() {
        Navegacion.mostrar(stage(), "/vista/MicuentaMisdatos.fxml");
    }

    @FXML
    private void irHistorial() {
        Navegacion.mostrar(stage(), "/vista/MicuentaHistorialdecompras.fxml");
    }

    @FXML
    private void irDeseos() {
        Navegacion.mostrar(stage(), "/vista/MicuentaListadedeseos.fxml");
    }

    @FXML
    private void cerrarSesion() {
        if (Navegacion.confirmarCerrarSesion()) {
            SesionActiva.cerrarSesion();
            Navegacion.mostrar(stage(), "/vista/Inicio de sesion.fxml");
        }
    }

    @FXML
    private void irCarrito() {
        Navegacion.mostrar(stage(), "/vista/Carrito.fxml");
    }

    @FXML
    private void regresar() {
        Navegacion.mostrar(stage(), "/vista/Interfaz de usuario.fxml");
    }

    private Stage stage() {
        return (Stage) lblResultados.getScene().getWindow();
    }
}

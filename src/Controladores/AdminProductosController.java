package Controladores;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import mercado67.modelo.ListaEnlazada;
import mercado67.modelo.Producto;
import mercado67.modelo.SesionActiva;
import vista.Navegacion;

public class AdminProductosController implements Initializable {

    @FXML private Label lblResultados;
    @FXML private Pane fila1;
    @FXML private Pane fila2;
    @FXML private Pane fila3;
    @FXML private Pane fila4;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        actualizar();
    }

    private void actualizar() {
        ListaEnlazada<Producto> lista = SesionActiva.getServicioProductos().listarProductos();
        lblResultados.setText(lista.tamano() == 0 ? "No hay productos" : "Total: " + lista.tamano() + " (se muestran 4)");
        Pane[] filas = {fila1, fila2, fila3, fila4};
        for (int i = 0; i < filas.length; i++) {
            pintar(filas[i], lista.obtener(i));
        }
    }

    private void pintar(Pane fila, Producto p) {
        if (p == null) {
            fila.setVisible(false);
            return;
        }
        fila.setVisible(true);
        Pane caja = (Pane) fila.getChildren().get(0);
        ImageView img = (ImageView) caja.getChildren().get(0);
        ((Label) fila.getChildren().get(1)).setText(p.getNombre());
        ((Label) fila.getChildren().get(2)).setText("$" + (int) p.getPrecio());
        ((Label) fila.getChildren().get(3)).setText(String.valueOf(p.getCantidad()));
        img.setImage(cargarFoto(p));
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

    private void eliminar(int posicion) {
        Producto p = SesionActiva.getServicioProductos().listarProductos().obtener(posicion);
        if (p == null) {
            return;
        }
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Eliminar producto");
        alerta.setHeaderText(null);
        alerta.setContentText("Eliminar " + p.getNombre() + "?");
        if (alerta.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            SesionActiva.getServicioProductos().eliminarProducto(p.getIdProducto());
            actualizar();
        }
    }

    private void editar(int posicion) {
        Producto p = SesionActiva.getServicioProductos().listarProductos().obtener(posicion);
        if (p == null) {
            return;
        }
        SesionActiva.setProductoSeleccionado(p);
        Navegacion.mostrar(stage(), "/vista/AdminEditarproducto.fxml");
    }

    @FXML private void eliminar1() { eliminar(0); }
    @FXML private void eliminar2() { eliminar(1); }
    @FXML private void eliminar3() { eliminar(2); }
    @FXML private void eliminar4() { eliminar(3); }
    @FXML private void editar1() { editar(0); }
    @FXML private void editar2() { editar(1); }
    @FXML private void editar3() { editar(2); }
    @FXML private void editar4() { editar(3); }

    @FXML private void irProductos() { Navegacion.mostrar(stage(), "/vista/AdminProductos.fxml"); }
    @FXML private void irUsuarios() { Navegacion.mostrar(stage(), "/vista/AdminUsuarios.fxml"); }
    @FXML private void irPedidos() { Navegacion.mostrar(stage(), "/vista/AdminPedidos.fxml"); }

    @FXML
    private void cerrarSesion() {
        SesionActiva.cerrarSesion();
        Navegacion.mostrar(stage(), "/vista/Inicio de sesion.fxml");
    }

    private Stage stage() {
        return (Stage) lblResultados.getScene().getWindow();
    }
}
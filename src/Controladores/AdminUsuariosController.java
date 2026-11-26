package Controladores;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import mercado67.modelo.SesionActiva;
import mercado67.modelo.Usuario;
import vista.Navegacion;

public class AdminUsuariosController implements Initializable {

    @FXML private Label lblResultados;
    @FXML private Pane fila1;
    @FXML private Pane fila2;
    @FXML private Pane fila3;
    @FXML private Pane fila4;
    @FXML private Pane fila5;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        List<Usuario> lista = SesionActiva.getServicioUsuarios().obtenerTodos();
        lblResultados.setText("Total: " + lista.size() + " (se muestran 5)");
        Pane[] filas = {fila1, fila2, fila3, fila4, fila5};
        for (int i = 0; i < filas.length; i++) {
            if (i >= lista.size()) {
                filas[i].setVisible(false);
                continue;
            }
            Usuario u = lista.get(i);
            ((Label) filas[i].getChildren().get(0)).setText(u.getNombre());
            ((Label) filas[i].getChildren().get(1)).setText(u.getCorreo());
            ((Label) filas[i].getChildren().get(2)).setText(u.isAdmin() ? "Administrador" : "Cliente");
        }
    }

    @FXML private void irProductos() { Navegacion.mostrar(stage(), "/vista/AdminProductos.fxml"); }
    @FXML private void irUsuarios() { Navegacion.mostrar(stage(), "/vista/AdminUsuarios.fxml"); }
    @FXML private void irPedidos() { Navegacion.mostrar(stage(), "/vista/AdminPedidos.fxml"); }

    @FXML
    private void cerrarSesion() {
        if (Navegacion.confirmarCerrarSesion()) {
            SesionActiva.cerrarSesion();
            Navegacion.mostrar(stage(), "/vista/Inicio de sesion.fxml");
        }
    }

    private Stage stage() {
        return (Stage) lblResultados.getScene().getWindow();
    }
}
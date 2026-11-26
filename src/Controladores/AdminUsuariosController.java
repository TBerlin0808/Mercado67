package Controladores;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import mercado67.modelo.SesionActiva;
import mercado67.modelo.Usuario;
import vista.Navegacion;

public class AdminUsuariosController implements Initializable {

    private static final int POR_PAGINA = 5;

    @FXML private Label lblResultados;
    @FXML private Label lblPaginaUsuarios;
    @FXML private Label lblTotalPaginasUsuarios;
    @FXML private Button btnAnteriorUsuarios;
    @FXML private Button btnSiguienteUsuarios;
    @FXML private Pane fila1;
    @FXML private Pane fila2;
    @FXML private Pane fila3;
    @FXML private Pane fila4;
    @FXML private Pane fila5;
    @FXML private Label u1Nombre;
    @FXML private Label u1Correo;
    @FXML private Label u1Estado;
    @FXML private Label u2Nombre;
    @FXML private Label u2Correo;
    @FXML private Label u2Estado;
    @FXML private Label u3Nombre;
    @FXML private Label u3Correo;
    @FXML private Label u3Estado;
    @FXML private Label u4Nombre;
    @FXML private Label u4Correo;
    @FXML private Label u4Estado;
    @FXML private Label u5Nombre;
    @FXML private Label u5Correo;
    @FXML private Label u5Estado;
    @FXML private Button u1Boton;
    @FXML private Button u2Boton;
    @FXML private Button u3Boton;
    @FXML private Button u4Boton;
    @FXML private Button u5Boton;

    private List<Usuario> usuarios = new ArrayList<>();
    private int pagina = 0;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        actualizar();
    }

    private void actualizar() {
        usuarios = SesionActiva.getServicioUsuarios().obtenerTodos();
        int total = usuarios.size();
        int totalPaginas = Math.max(1, (total + POR_PAGINA - 1) / POR_PAGINA);
        if (pagina >= totalPaginas) {
            pagina = totalPaginas - 1;
        }
        if (pagina < 0) {
            pagina = 0;
        }

        lblResultados.setText("Total: " + total);
        lblPaginaUsuarios.setText(String.valueOf(pagina + 1));
        lblTotalPaginasUsuarios.setText(String.valueOf(totalPaginas));
        btnAnteriorUsuarios.setDisable(pagina == 0);
        btnSiguienteUsuarios.setDisable(pagina >= totalPaginas - 1);

        Pane[] filas = {fila1, fila2, fila3, fila4, fila5};
        Label[] nombres = {u1Nombre, u2Nombre, u3Nombre, u4Nombre, u5Nombre};
        Label[] correos = {u1Correo, u2Correo, u3Correo, u4Correo, u5Correo};
        Label[] estados = {u1Estado, u2Estado, u3Estado, u4Estado, u5Estado};
        Button[] botones = {u1Boton, u2Boton, u3Boton, u4Boton, u5Boton};

        for (int i = 0; i < filas.length; i++) {
            int indice = pagina * POR_PAGINA + i;
            if (indice >= usuarios.size()) {
                filas[i].setVisible(false);
                continue;
            }
            Usuario u = usuarios.get(indice);
            filas[i].setVisible(true);
            nombres[i].setText(u.getNombre());
            correos[i].setText(u.getCorreo());
            estados[i].setText((u.isAdmin() ? "Administrador" : "Cliente")
                    + " - " + (u.isActivo() ? "Activo" : "Inactivo"));
            botones[i].setText(u.isActivo() ? "Desactivar" : "Activar");
            botones[i].setDisable(u.isAdmin());
        }
    }

    private void cambiarEstado(int fila) {
        int indice = pagina * POR_PAGINA + fila;
        if (indice >= usuarios.size()) {
            return;
        }
        Usuario u = usuarios.get(indice);
        if (u.isAdmin()) {
            mostrar(Alert.AlertType.WARNING, "Usuario protegido", "No se puede desactivar una cuenta de administrador.");
            return;
        }
        boolean activar = !u.isActivo();
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle(activar ? "Activar usuario" : "Desactivar usuario");
        alerta.setHeaderText(null);
        alerta.setContentText((activar ? "Activar a " : "Desactivar a ") + u.getNombre() + "?");
        if (alerta.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            SesionActiva.getServicioUsuarios().establecerActivo(u, activar);
            actualizar();
        }
    }

    @FXML private void cambiarEstado1() { cambiarEstado(0); }
    @FXML private void cambiarEstado2() { cambiarEstado(1); }
    @FXML private void cambiarEstado3() { cambiarEstado(2); }
    @FXML private void cambiarEstado4() { cambiarEstado(3); }
    @FXML private void cambiarEstado5() { cambiarEstado(4); }

    @FXML
    private void paginaAnterior() {
        if (pagina > 0) {
            pagina--;
            actualizar();
        }
    }

    @FXML
    private void paginaSiguiente() {
        pagina++;
        actualizar();
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

    private void mostrar(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}

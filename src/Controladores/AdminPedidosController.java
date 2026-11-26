package Controladores;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import mercado67.modelo.ListaEnlazada;
import mercado67.modelo.Pedido;
import mercado67.modelo.SesionActiva;
import mercado67.modelo.Usuario;
import vista.Navegacion;

public class AdminPedidosController implements Initializable {

    @FXML private Label lblResultados;
    @FXML private Pane fila1;
    @FXML private Pane fila2;
    @FXML private Pane fila3;
    @FXML private Pane fila4;
    @FXML private Pane fila5;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ListaEnlazada<Pedido> lista = SesionActiva.getServicioPedidos().todos();
        int n = lista.tamano();
        lblResultados.setText(n == 0 ? "No hay pedidos" : "Total: " + n + " (los 5 mas recientes)");
        Pane[] filas = {fila1, fila2, fila3, fila4, fila5};
        for (int i = 0; i < filas.length; i++) {
            Pedido p = lista.obtener(n - 1 - i);
            if (p == null) {
                filas[i].setVisible(false);
                continue;
            }
            ((Label) filas[i].getChildren().get(0)).setText(String.valueOf(p.getIdPedido()));
            ((Label) filas[i].getChildren().get(1)).setText(nombreCliente(p.getIdComprador()));
            ((Label) filas[i].getChildren().get(2)).setText(p.getFecha().toString());
            ((Label) filas[i].getChildren().get(3)).setText("$" + (int) p.getTotal());
            ((Label) filas[i].getChildren().get(4)).setText(String.valueOf(p.getItemsComprados().size()));
        }
    }

    private String nombreCliente(int idComprador) {
        for (Usuario u : SesionActiva.getServicioUsuarios().obtenerTodos()) {
            if (u.getNombre().hashCode() == idComprador) {
                return u.getNombre();
            }
        }
        return "Desconocido";
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
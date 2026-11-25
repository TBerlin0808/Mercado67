package Controladores;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import mercado67.modelo.ItemCarrito;
import mercado67.modelo.ListaEnlazada;
import mercado67.modelo.Pedido;
import mercado67.modelo.SesionActiva;
import vista.Navegacion;

public class MicuentaHistorialdecomprasController implements Initializable {

    @FXML private Label lblResultados;
    @FXML private Pane pedido1;
    @FXML private Pane pedido2;
    @FXML private Label p1Titulo;
    @FXML private Label p1Fecha;
    @FXML private Label p1Estado;
    @FXML private Label p1Nombre1;
    @FXML private Label p1Detalle1;
    @FXML private Label p1Sub1;
    @FXML private Label p1Nombre2;
    @FXML private Label p1Detalle2;
    @FXML private Label p1Sub2;
    @FXML private Label p1Total;
    @FXML private Label p2Titulo;
    @FXML private Label p2Fecha;
    @FXML private Label p2Estado;
    @FXML private Label p2Nombre1;
    @FXML private Label p2Detalle1;
    @FXML private Label p2Sub1;
    @FXML private Label p2Nombre2;
    @FXML private Label p2Detalle2;
    @FXML private Label p2Sub2;
    @FXML private Label p2Total;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        pedido1.setVisible(false);
        pedido2.setVisible(false);
        if (SesionActiva.getUsuarioActual() == null) {
            return;
        }
        ListaEnlazada<Pedido> mios = SesionActiva.getServicioPedidos().pedidosDe(SesionActiva.getUsuarioActual());
        int n = mios.tamano();
        lblResultados.setText(n == 0 ? "Aun no tienes compras" : "Compras realizadas: " + n);
        pintar(mios.obtener(n - 1), pedido1, p1Titulo, p1Fecha, p1Estado, p1Nombre1, p1Detalle1, p1Sub1, p1Nombre2, p1Detalle2, p1Sub2, p1Total);
        pintar(mios.obtener(n - 2), pedido2, p2Titulo, p2Fecha, p2Estado, p2Nombre1, p2Detalle1, p2Sub1, p2Nombre2, p2Detalle2, p2Sub2, p2Total);
    }

    private void pintar(Pedido pedido, Pane panel, Label titulo, Label fecha, Label estado,
            Label n1, Label d1, Label s1, Label n2, Label d2, Label s2, Label total) {
        if (pedido == null) {
            return;
        }
        panel.setVisible(true);
        titulo.setText("Pedido N " + pedido.getIdPedido());
        fecha.setText(pedido.getFecha().toString());
        estado.setText(pedido.getEstado());
        total.setText("$" + (int) pedido.getTotal());
        item(pedido, 0, n1, d1, s1);
        item(pedido, 1, n2, d2, s2);
    }

    private void item(Pedido pedido, int posicion, Label nombre, Label detalle, Label subtotal) {
        if (posicion >= pedido.getItemsComprados().size()) {
            return;
        }
        ItemCarrito it = pedido.getItemsComprados().get(posicion);
        nombre.setText(it.getProducto().getNombre());
        detalle.setText(it.getCantidad() + " x $" + (int) it.getProducto().getPrecio());
        subtotal.setText("$" + (int) it.getSubtotal());
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
        SesionActiva.cerrarSesion();
        Navegacion.mostrar(stage(), "/vista/Inicio de sesion.fxml");
    }

    @FXML
    private void regresar() {
        Navegacion.mostrar(stage(), "/vista/Interfaz de usuario.fxml");
    }

    private Stage stage() {
        return (Stage) lblResultados.getScene().getWindow();
    }
}
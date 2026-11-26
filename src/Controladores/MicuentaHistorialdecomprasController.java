package Controladores;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import mercado67.modelo.ItemCarrito;
import mercado67.modelo.ListaEnlazada;
import mercado67.modelo.Pedido;
import mercado67.modelo.SesionActiva;
import vista.Navegacion;

public class MicuentaHistorialdecomprasController implements Initializable {

    private static final int PEDIDOS_POR_PAGINA = 2;

    @FXML private Label lblResultados;
    @FXML private Label lblPaginaPedidos;
    @FXML private Label lblTotalPaginasPedidos;
    @FXML private Button btnAnteriorPedidos;
    @FXML private Button btnSiguientePedidos;
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

    private int pagina = 0;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        actualizar();
    }

    private void actualizar() {
        pedido1.setVisible(false);
        pedido2.setVisible(false);

        if (SesionActiva.getUsuarioActual() == null) {
            lblPaginaPedidos.setText("1");
            lblTotalPaginasPedidos.setText("1");
            btnAnteriorPedidos.setDisable(true);
            btnSiguientePedidos.setDisable(true);
            return;
        }

        ListaEnlazada<Pedido> mios = SesionActiva.getServicioPedidos().pedidosDe(SesionActiva.getUsuarioActual());
        int total = mios.tamano();
        int totalPaginas = Math.max(1, (total + PEDIDOS_POR_PAGINA - 1) / PEDIDOS_POR_PAGINA);
        if (pagina >= totalPaginas) {
            pagina = totalPaginas - 1;
        }
        if (pagina < 0) {
            pagina = 0;
        }

        lblResultados.setText(total == 0 ? "Aun no tienes compras" : "Compras realizadas: " + total);
        lblPaginaPedidos.setText(String.valueOf(pagina + 1));
        lblTotalPaginasPedidos.setText(String.valueOf(totalPaginas));
        btnAnteriorPedidos.setDisable(pagina == 0);
        btnSiguientePedidos.setDisable(pagina >= totalPaginas - 1);

        int posicion1 = total - 1 - (pagina * PEDIDOS_POR_PAGINA);
        int posicion2 = posicion1 - 1;

        pintar(posicion1 >= 0 ? mios.obtener(posicion1) : null, pedido1,
                p1Titulo, p1Fecha, p1Estado, p1Nombre1, p1Detalle1, p1Sub1,
                p1Nombre2, p1Detalle2, p1Sub2, p1Total);
        pintar(posicion2 >= 0 ? mios.obtener(posicion2) : null, pedido2,
                p2Titulo, p2Fecha, p2Estado, p2Nombre1, p2Detalle1, p2Sub1,
                p2Nombre2, p2Detalle2, p2Sub2, p2Total);
    }

    private void pintar(Pedido pedido, Pane panel, Label titulo, Label fecha, Label estado,
            Label n1, Label d1, Label s1, Label n2, Label d2, Label s2, Label total) {
        titulo.setText("");
        fecha.setText("");
        estado.setText("");
        total.setText("");
        n1.setText("");
        d1.setText("");
        s1.setText("");
        n2.setText("");
        d2.setText("");
        s2.setText("");

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
    private void paginaAnterior() {
        if (pagina > 0) {
            pagina--;
            actualizar();
        }
    }

    @FXML
    private void paginaSiguiente() {
        if (SesionActiva.getUsuarioActual() == null) {
            return;
        }
        int totalPaginas = Math.max(1,
                (SesionActiva.getServicioPedidos().pedidosDe(SesionActiva.getUsuarioActual()).tamano()
                + PEDIDOS_POR_PAGINA - 1) / PEDIDOS_POR_PAGINA);
        if (pagina < totalPaginas - 1) {
            pagina++;
            actualizar();
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

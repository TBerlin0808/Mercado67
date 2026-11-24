/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controladores;
import vista.Navegacion;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import mercado67.modelo.Carrito;
import mercado67.modelo.ItemCarrito;
import mercado67.modelo.Pedido;
import mercado67.modelo.SesionActiva;
import mercado67.modelo.Usuario;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Miguel
 */
public class CarritoController implements Initializable {

    @FXML
    private Label lblResultados;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        actualizarCarrito();
    }

    private void actualizarCarrito() {
        Carrito carrito = SesionActiva.getCarrito();
        int totalItems = carrito.cantidadItems();
        double total = carrito.getTotal();
        if (lblResultados != null) {
            lblResultados.setText("Items: " + totalItems + " | Total: $" + (int) total);
        }
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
            mostrar(Alert.AlertType.ERROR, "Error", "No se pudo regresar a la pantalla principal.");
        }
    }

    @FXML
    private void pagar() {
        Carrito carrito = SesionActiva.getCarrito();
        if (carrito.cantidadItems() == 0) {
            mostrar(Alert.AlertType.WARNING, "Carrito vacio", "No hay productos en el carrito.");
            return;
        }

        Usuario comprador = SesionActiva.getUsuarioActual();
        if (comprador == null) {
            mostrar(Alert.AlertType.ERROR, "Error", "Debes iniciar sesion para pagar.");
            return;
        }

        List<ItemCarrito> items = new ArrayList<>();
        for (ItemCarrito item : carrito.getItems()) {
            items.add(item);
        }

        Pedido pedido = new Pedido(
                (int) (System.currentTimeMillis() / 1000),
                comprador.getNombre().hashCode(),
                LocalDate.now(),
                "Pendiente",
                items
        );

        // Guardar pedido
        List<Map<String, Object>> listaPedidos = new ArrayList<>();
        // Nota: En una version futura se podria cargar pedidos existentes desde JSON
        Map<String, Object> p = new java.util.LinkedHashMap<>();
        p.put("idPedido", pedido.getIdPedido());
        p.put("idComprador", pedido.getIdComprador());
        p.put("fecha", pedido.getFecha().toString());
        p.put("estado", pedido.getEstado());
        p.put("total", pedido.getTotal());
        listaPedidos.add(p);

        mercado67.modelo.UtilJSON.escribirArchivo(
                "data/pedidos.json",
                mercado67.modelo.UtilJSON.listaAJSON(listaPedidos)
        );

        carrito.vaciar();
        actualizarCarrito();

        mostrar(Alert.AlertType.INFORMATION, "Pago exitoso", "Tu pedido se ha registrado correctamente. Total: $" + (int) pedido.getTotal());

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/vista/Interfaz de usuario.fxml"));
            Stage stage = (Stage) lblResultados.getScene().getWindow();
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

package vista;

import java.io.IOException;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Centraliza el cambio de pantallas y hace que el contenido se adapte
 * proporcionalmente al tamano de la ventana.
 */
public final class Navegacion {

    private static final double ANCHO_INICIAL = 1100;
    private static final double ALTO_INICIAL = 720;
    private static final String COLOR_FONDO = "#dfe6f5";

    private Navegacion() {
    }

    /**
     * Carga un FXML y lo muestra en la ventana indicada.
     */
    public static void mostrar(Stage stage, String ruta) {
        try {
            Parent root = FXMLLoader.load(Navegacion.class.getResource(ruta));
            mostrar(stage, root);
        } catch (IOException e) {
            mostrarError("No se pudo abrir la pantalla.");
        }
    }

    /**
     * Muestra un nodo raiz ya cargado y lo escala para llenar la ventana
     * manteniendo su relacion de aspecto.
     */
    public static void mostrar(Stage stage, Parent root) {
        Group grupo = new Group(root);
        StackPane contenedor = new StackPane(grupo);
        contenedor.setStyle("-fx-background-color: " + COLOR_FONDO + ";");

        double anchoPrevio = (stage.getScene() != null) ? stage.getScene().getWidth() : 0;
        double altoPrevio = (stage.getScene() != null) ? stage.getScene().getHeight() : 0;
        boolean maximizado = stage.isMaximized();

        Scene scene = new Scene(contenedor);

        Runnable ajustar = () -> {
            // Usar el tamano real del contenido (incluye hijos que se salen
            // del AnchorPane), no solo el prefWidth/prefHeight declarado.
            double anchoContenido = grupo.getLayoutBounds().getWidth();
            double altoContenido = grupo.getLayoutBounds().getHeight();
            if (anchoContenido <= 0 || altoContenido <= 0) {
                anchoContenido = anchoDiseno(root);
                altoContenido = altoDiseno(root);
            }
            if (anchoContenido <= 0 || altoContenido <= 0 || scene.getWidth() <= 0 || scene.getHeight() <= 0) {
                return;
            }
            // 0.98 deja un pequeno margen para que ningun borde quede cortado.
            double factor = Math.min(scene.getWidth() / anchoContenido, scene.getHeight() / altoContenido) * 0.98;
            grupo.setScaleX(factor);
            grupo.setScaleY(factor);
        };

        scene.widthProperty().addListener((observable, anterior, nuevo) -> ajustar.run());
        scene.heightProperty().addListener((observable, anterior, nuevo) -> ajustar.run());

        stage.setScene(scene);

        if (maximizado) {
            stage.setMaximized(true);
        } else if (anchoPrevio > 0 && altoPrevio > 0) {
            stage.setWidth(anchoPrevio);
            stage.setHeight(altoPrevio);
        } else {
            stage.setWidth(Math.max(ANCHO_INICIAL, anchoDiseno(root)));
            stage.setHeight(Math.max(ALTO_INICIAL, altoDiseno(root)));
        }

        Platform.runLater(ajustar);
    }

    /**
     * Muestra un dialogo de confirmacion antes de cerrar la sesion.
     * Retorna true solo si el usuario pulsa "Si".
     */
    public static boolean confirmarCerrarSesion() {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Cerrar sesión");
        alerta.setHeaderText(null);
        alerta.setContentText("¿Estás seguro que quieres cerrar sesión?");
        ButtonType si = new ButtonType("Sí", ButtonBar.ButtonData.YES);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.NO);
        alerta.getButtonTypes().setAll(si, no);
        return alerta.showAndWait().orElse(no) == si;
    }

    private static double anchoDiseno(Parent root) {
        if (root instanceof Region) {
            double ancho = ((Region) root).prefWidth(-1);
            if (ancho > 0) {
                return ancho;
            }
        }
        return root.getLayoutBounds().getWidth();
    }

    private static double altoDiseno(Parent root) {
        if (root instanceof Region) {
            double alto = ((Region) root).prefHeight(-1);
            if (alto > 0) {
                return alto;
            }
        }
        return root.getLayoutBounds().getHeight();
    }

    private static void mostrarError(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Error");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}

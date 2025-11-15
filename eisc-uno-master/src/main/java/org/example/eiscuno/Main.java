package org.example.eiscuno;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import java.io.IOException;

/**
 * The main class of the Cincuentazo application.
 * Entry point for the JavaFX application.
 *
 * @author Jairo Andrés Tegue
 * @version 1.0
 * @since 2025
 */
public class Main extends Application {

    /**
     * The main method of the application.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Starts the application with player selection screen.
     *
     * @param primaryStage the primary stage of the application
     * @throws IOException if an error occurs while loading the FXML
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/eiscuno/player-selection-view.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);

        Image appIcon = new Image(getClass().getResourceAsStream("/org/example/eiscuno/images/Fondo_50ZO.png"));
        primaryStage.getIcons().add(appIcon);
        primaryStage.setTitle("Cincuentazo - Seleccion de Jugadores");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
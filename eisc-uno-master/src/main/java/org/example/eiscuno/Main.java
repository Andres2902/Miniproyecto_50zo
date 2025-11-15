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
 *
 * @author Jairo A. Tegue
 * @version 1.0
 * @since 202
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
        // Load the player selection screen instead of directly loading the game
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/eiscuno/player-selection-view.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);

        Image appIcon = new Image(getClass().getResourceAsStream("/org/example/eiscuno/images/Fondo_50ZO.png"));
        primaryStage.getIcons().add(appIcon);
        primaryStage.setTitle("Cincuentazo - Selección de Jugadores");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
package org.example.cincuentazo.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Represents the main stage of the Cincuentazo game application.
 * This stage displays the game interface to the user.
 *
 * @author Jairo Andrés Tegue
 * @version 1.0
 * @since 2025
 */
public class Game50zoStage extends Stage {

    /**
     * Constructs a new instance of Game50zoStage.
     *
     * @throws IOException if an error occurs while loading the FXML file for the game interface
     */
    public Game50zoStage() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/cincuentazo/game-uno-view.fxml"));
        Parent root;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new IOException("Error al cargar el archivo FXML", e);
        }
        Scene scene = new Scene(root);
        setTitle("EISC Cincuentazo");
        setScene(scene);
        setResizable(false);
        show();
    }

    /**
     * Closes the instance of Game50zoStage.
     * This method is used to clean up resources when the game stage is no longer needed.
     */
    public static void deleteInstance() {
        GameUnoStageHolder.INSTANCE.close();
        GameUnoStageHolder.INSTANCE = null;
    }

    /**
     * Retrieves the singleton instance of Game50zoStage.
     *
     * @return the singleton instance of Game50zoStage
     * @throws IOException if an error occurs while creating the instance
     */
    public static Game50zoStage getInstance() throws IOException {
        return GameUnoStageHolder.INSTANCE != null ?
                GameUnoStageHolder.INSTANCE :
                (GameUnoStageHolder.INSTANCE = new Game50zoStage());
    }

    /**
     * Holder class for the singleton instance of Game50zoStage.
     * This class ensures lazy initialization of the singleton instance.
     */
    private static class GameUnoStageHolder {
        private static Game50zoStage INSTANCE;
    }
}
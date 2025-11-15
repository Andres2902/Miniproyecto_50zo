package org.example.eiscuno.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Controller for the player selection screen in Cincuentazo game.
 * Allows the user to select the number of machine players (1-3) before starting the game.
 *
 * @author Jairo Andrés Tegue
 * @version 1.0
 * @since 2025
 */
public class PlayerSelectionController {

    @FXML
    private Button btnOneMachine;

    @FXML
    private Button btnTwoMachines;

    @FXML
    private Button btnThreeMachines;

    /**
     * Initializes the controller and sets up button actions.
     * Called automatically after the FXML file has been loaded.
     */
    @FXML
    public void initialize() {
        setupButtonActions();
    }

    /**
     * Sets up the action handlers for the machine player selection buttons.
     */
    private void setupButtonActions() {
        btnOneMachine.setOnAction(event -> startGameWithMachines(1));
        btnTwoMachines.setOnAction(event -> startGameWithMachines(2));
        btnThreeMachines.setOnAction(event -> startGameWithMachines(3));
    }

    /**
     * Starts the game with the selected number of machine players.
     *
     * @param numberOfMachines the number of machine players (1-3)
     */
    private void startGameWithMachines(int numberOfMachines) {
        try {
            Stage currentStage = (Stage) btnOneMachine.getScene().getWindow();
            currentStage.close();

            startMainGame(numberOfMachines);
        } catch (IOException e) {
            System.err.println("Error starting game: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Loads and starts the main game window with the specified number of machine players.
     *
     * @param numberOfMachines the number of machine players
     * @throws IOException if the FXML file cannot be loaded
     */
    private void startMainGame(int numberOfMachines) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/eiscuno/game-uno-view.fxml"));
        Parent root = loader.load();

        GameUnoController mainController = loader.getController();
        mainController.setNumberOfMachinePlayers(numberOfMachines);

        Stage gameStage = new Stage();
        Image appIcon = new Image(getClass().getResourceAsStream("/org/example/eiscuno/images/Fondo_50ZO.png"));
        gameStage.getIcons().add(appIcon);
        gameStage.setTitle("Cincuentazo - " + numberOfMachines + " Maquina(s)");
        gameStage.setScene(new Scene(root));
        gameStage.setResizable(false);
        gameStage.show();
    }
}
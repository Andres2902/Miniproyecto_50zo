package org.example.eiscuno.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.game.GameUnoModel;
import org.example.eiscuno.model.machine.ThreadMachinePlayer;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;
import org.example.eiscuno.model.exceptions.PlayerEliminatedException;
import org.example.eiscuno.model.exceptions.InvalidCardException;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Main controller for the Cincuentazo game.
 * Manages game flow, UI updates and user interactions following the MVC pattern.
 *
 * @author Jairo Andrés Tegue
 * @version 1.0
 * @since 2025
 */
public class GameUnoController implements Observer {

    @FXML private GridPane gridPaneCardsPlayer;
    @FXML private ImageView tableImageView;
    @FXML private Label lblTableSum;
    @FXML private Label lblCurrentPlayer;
    @FXML private Label lblGameStatus;
    @FXML private Label lblTimer;
    @FXML private Label lblCardsRemaining;
    @FXML private Label lblCardsOnTable;
    @FXML private Label lblHumanCards;
    @FXML private Label lblActivePlayers;
    @FXML private ProgressBar progressSum;
    @FXML private HBox machinePlayersContainer;

    private GameUnoModel gameModel;
    private GameTimer gameTimer;
    private int posInitCardToShow;
    private int numberOfMachinePlayers;
    private List<ThreadMachinePlayer> machineThreads;
    private boolean isHumanTurn;

    /**
     * Initializes the controller.
     * Called automatically after the FXML file has been loaded.
     */
    @FXML
    public void initialize() {
        this.posInitCardToShow = 0;
        this.machineThreads = new ArrayList<>();
        this.gameTimer = new GameTimer();
        this.gameTimer.addObserver(this);
        this.isHumanTurn = false;

        initializeUI();
    }

    /**
     * Initializes the user interface with default values.
     */
    private void initializeUI() {
        lblGameStatus.setText("Selecciona una carta para jugar");
        lblTimer.setText("Tiempo: 00:00");
        lblCardsRemaining.setText("52");
        lblCardsOnTable.setText("0");
        lblHumanCards.setText("Tus cartas: 0");
        lblActivePlayers.setText("Jugadores activos: 0");
        progressSum.setProgress(0.0);
    }

    /**
     * Sets the number of machine players and initializes the game.
     *
     * @param numberOfMachinePlayers the number of machine players (1-3)
     */
    public void setNumberOfMachinePlayers(int numberOfMachinePlayers) {
        this.numberOfMachinePlayers = numberOfMachinePlayers;
        initializeGame();
    }

    /**
     * Initializes the game with players, deck, and table.
     * Starts the game timer and machine player threads.
     */
    private void initializeGame() {
        try {
            Player humanPlayer = new Player("JUGADOR_HUMANO");
            Deck deck = new Deck();
            Table table = new Table();

            this.gameModel = new GameUnoModel(humanPlayer, numberOfMachinePlayers, deck, table);
            this.gameModel.addObserver(this);
            this.gameModel.startGame();

            gameTimer.startTimer();

            this.isHumanTurn = gameModel.getCurrentPlayer().getTypePlayer().startsWith("JUGADOR_HUMANO");
            gameTimer.startTurnTimer("Jugador Humano");

            updateUI();
            startMachineThreads();
            updateCurrentPlayerIndicator();

        } catch (Exception e) {
            showErrorAlert("Error al inicializar el juego", e.getMessage());
        }
    }

    /**
     * Starts the threads for all machine players.
     */
    private void startMachineThreads() {
        for (int i = 0; i < numberOfMachinePlayers; i++) {
            ThreadMachinePlayer machineThread = new ThreadMachinePlayer(
                    gameModel.getMachinePlayers().get(i),
                    gameModel,
                    tableImageView
            );
            machineThreads.add(machineThread);
            machineThread.start();
        }
    }

    /**
     * Updates all UI components with current game state.
     */
    private void updateUI() {
        printCardsHumanPlayer();
        updateTableInfo();
        updateMachinePlayersDisplay();
        updatePlayerInfo();
        updateProgressBar();
        lblGameStatus.setText(gameModel.getGameStatus());

        ensureTableCardDisplayed();

        if (gameModel != null && gameModel.getCurrentPlayer() != null) {
            this.isHumanTurn = gameModel.getCurrentPlayer().getTypePlayer().startsWith("JUGADOR_HUMANO");
        }
    }

    /**
     * Displays the human player's cards in the grid pane.
     * Shows visual indicators for playable cards (green) and unplayable cards (red).
     */
    private void printCardsHumanPlayer() {
        this.gridPaneCardsPlayer.getChildren().clear();

        if (gameModel.isPlayerEliminated(gameModel.getHumanPlayer())) {
            Label eliminatedLabel = new Label("ELIMINADO - No puedes jugar más cartas");
            eliminatedLabel.setStyle("-fx-text-fill: red; -fx-font-size: 16px; -fx-font-weight: bold;");
            this.gridPaneCardsPlayer.add(eliminatedLabel, 0, 0);
            return;
        }

        Card[] currentVisibleCards = this.gameModel.getCurrentVisibleCardsHumanPlayer(this.posInitCardToShow);

        for (int i = 0; i < currentVisibleCards.length; i++) {
            Card card = currentVisibleCards[i];
            ImageView cardImageView = card.getCard();

            boolean canPlayCard = card.canBePlayed(gameModel.getCurrentSum());

            if (isHumanTurn && canPlayCard && !gameModel.isPlayerEliminated(gameModel.getHumanPlayer())) {
                cardImageView.setStyle("-fx-effect: dropshadow(gaussian, #00ff00, 15, 0.7, 0, 0); -fx-cursor: hand;");
                cardImageView.setOnMouseClicked((MouseEvent event) -> {
                    handleCardPlay(card);
                });

                cardImageView.setOnMouseEntered(event -> {
                    cardImageView.setScaleX(1.1);
                    cardImageView.setScaleY(1.1);
                });

                cardImageView.setOnMouseExited(event -> {
                    cardImageView.setScaleX(1.0);
                    cardImageView.setScaleY(1.0);
                });
            } else {
                cardImageView.setStyle("-fx-effect: dropshadow(gaussian, #ff0000, 15, 0.7, 0, 0); -fx-cursor: not-allowed;");
                cardImageView.setOnMouseClicked(null);
                cardImageView.setOnMouseEntered(null);
                cardImageView.setOnMouseExited(null);
            }

            this.gridPaneCardsPlayer.add(cardImageView, i, 0);
        }
    }

    /**
     * Handles the card play action when a player clicks on a card.
     *
     * @param card the card to be played
     */
    private void handleCardPlay(Card card) {
        try {
            if (gameModel == null) {
                showErrorAlert("Juego no inicializado", "El juego no se ha inicializado correctamente.");
                return;
            }

            if (!isHumanTurn) {
                showWarningAlert("No es tu turno", "Espera tu turno para jugar una carta.");
                return;
            }

            if (card.canBePlayed(gameModel.getCurrentSum())) {
                gameModel.playCard(card, gameModel.getHumanPlayer());
                tableImageView.setImage(card.getImage());

                updateUI();

                try {
                    gameModel.takeCardFromDeck(gameModel.getHumanPlayer());
                    updateUI();

                    if (!gameModel.canPlayerPlay(gameModel.getHumanPlayer())) {
                        showInformationAlert("Jugador Eliminado", "¡No puedes jugar ninguna carta. Has sido eliminado!");
                        gameModel.eliminatePlayer(gameModel.getHumanPlayer());
                        updateUI();
                    }

                } catch (Exception e) {
                    System.err.println("Error al tomar carta después de jugar: " + e.getMessage());
                }

                if (!gameModel.isPlayerEliminated(gameModel.getHumanPlayer())) {
                    gameModel.nextTurn();
                    updateCurrentPlayerIndicator();
                    gameTimer.startTurnTimer(gameModel.getCurrentPlayer().getTypePlayer());
                }

                if (gameModel.isGameOver()) {
                    handleGameOver();
                }

            } else {
                showWarningAlert("Movimiento inválido", "Esta carta haría que la suma exceda 50.");
            }
        } catch (PlayerEliminatedException e) {
            showErrorAlert("Jugador Eliminado", e.getMessage());
            updateUI();
        } catch (InvalidCardException e) {
            showErrorAlert("Carta Inválida", e.getMessage());
        } catch (Exception e) {
            showErrorAlert("Error al jugar carta", e.getMessage());
        }
    }

    /**
     * Handles the take card button action.
     * Allows the human player to take a card from the deck.
     *
     * @param event the action event
     */
    @FXML
    void onHandleTakeCard(ActionEvent event) {
        if (gameModel == null) {
            showErrorAlert("Juego no inicializado", "El juego no se ha inicializado correctamente.");
            return;
        }

        try {
            if (!isHumanTurn) {
                showWarningAlert("No es tu turno", "Solo puedes tomar cartas en tu turno.");
                return;
            }

            Card newCard = gameModel.takeCardFromDeck(gameModel.getHumanPlayer());
            printCardsHumanPlayer();
            showInformationAlert("Carta Tomada", "Has tomado una carta del mazo.");

            if (!gameModel.canPlayerPlay(gameModel.getHumanPlayer())) {
                showInformationAlert("Jugador Eliminado", "¡No puedes jugar ninguna carta. Has sido eliminado!");
                gameModel.eliminatePlayer(gameModel.getHumanPlayer());
                updateUI();
            } else {
                gameModel.nextTurn();
                updateCurrentPlayerIndicator();
            }

        } catch (Exception e) {
            showErrorAlert("Error al tomar carta", e.getMessage());
        }
    }

    /**
     * Updates the table information labels (sum, cards on table, cards remaining).
     */
    private void updateTableInfo() {
        if (gameModel == null) return;

        int currentSum = gameModel.getCurrentSum();
        lblTableSum.setText("Suma: " + currentSum + "/50");
        lblCardsOnTable.setText(String.valueOf(gameModel.getTable().getNumberOfCards()));
        lblCardsRemaining.setText(String.valueOf(gameModel.getDeck().size()));
    }

    /**
     * Updates the progress bar based on the current sum (0-50).
     * Changes color based on proximity to the limit.
     */
    private void updateProgressBar() {
        if (gameModel == null) return;

        double progress = gameModel.getCurrentSum() / 50.0;
        progressSum.setProgress(progress);

        if (progress > 0.8) {
            progressSum.setStyle("-fx-accent: #FF5252;");
        } else if (progress > 0.6) {
            progressSum.setStyle("-fx-accent: #FFA726;");
        } else {
            progressSum.setStyle("-fx-accent: #4CAF50;");
        }
    }

    /**
     * Updates player information labels (human cards count, active players count).
     */
    private void updatePlayerInfo() {
        if (gameModel == null) return;

        lblHumanCards.setText("Tus cartas: " + gameModel.getHumanPlayer().getCardsPlayer().size());
        int activePlayers = countActivePlayers();
        lblActivePlayers.setText("Jugadores activos: " + activePlayers);
    }

    /**
     * Counts the number of active (non-eliminated) players.
     *
     * @return the number of active players
     */
    private int countActivePlayers() {
        if (gameModel == null) return 0;

        int activePlayers = 0;

        if (!gameModel.isPlayerEliminated(gameModel.getHumanPlayer())) {
            activePlayers++;
        }

        for (Player machine : gameModel.getMachinePlayers()) {
            if (!gameModel.isPlayerEliminated(machine)) {
                activePlayers++;
            }
        }

        return activePlayers;
    }

    /**
     * Updates the display of machine players with their current status.
     */
    private void updateMachinePlayersDisplay() {
        machinePlayersContainer.getChildren().clear();

        if (gameModel == null) return;

        for (int i = 0; i < gameModel.getMachinePlayers().size(); i++) {
            Player machine = gameModel.getMachinePlayers().get(i);
            boolean isEliminated = gameModel.isPlayerEliminated(machine);

            String status;
            if (isEliminated) {
                status = "ELIMINADO";
            } else if (machine.equals(gameModel.getCurrentPlayer())) {
                status = "TURNO - " + machine.getCardsPlayer().size() + " cartas";
            } else {
                status = machine.getCardsPlayer().size() + " cartas";
            }

            Label machineLabel = new Label("Máquina " + (i + 1) + ": " + status);

            if (isEliminated) {
                machineLabel.setStyle("-fx-text-fill: #FF6B6B; -fx-font-weight: bold; -fx-padding: 5px;");
            } else if (machine.equals(gameModel.getCurrentPlayer())) {
                machineLabel.setStyle("-fx-text-fill: #FFD700; -fx-font-weight: bold; -fx-padding: 5px;");
            } else {
                machineLabel.setStyle("-fx-text-fill: #4ECDC4; -fx-font-weight: bold; -fx-padding: 5px;");
            }

            machinePlayersContainer.getChildren().add(machineLabel);
        }
    }

    /**
     * Updates the current player indicator label.
     */
    private void updateCurrentPlayerIndicator() {
        if (gameModel == null) return;

        Player currentPlayer = gameModel.getCurrentPlayer();
        if (currentPlayer.getTypePlayer().startsWith("JUGADOR_HUMANO")) {
            lblCurrentPlayer.setText("Turno: Jugador Humano");
            lblCurrentPlayer.setStyle("-fx-text-fill: #4ECDC4; -fx-font-weight: bold; -fx-font-size: 14px;");
            this.isHumanTurn = true;
        } else {
            lblCurrentPlayer.setText("Turno: " + currentPlayer.getTypePlayer());
            lblCurrentPlayer.setStyle("-fx-text-fill: #FF6B6B; -fx-font-weight: bold; -fx-font-size: 14px;");
            this.isHumanTurn = false;
        }
    }

    /**
     * Handles game over state.
     * Stops all threads and displays the winner.
     */
    private void handleGameOver() {
        for (ThreadMachinePlayer thread : machineThreads) {
            thread.stopThread();
        }

        gameTimer.stopTimer();

        Player winner = gameModel.determineWinner();
        String winnerName = winner != null ?
                (winner.getTypePlayer().startsWith("JUGADOR_HUMANO") ? "¡Jugador Humano!" : winner.getTypePlayer()) :
                "Sin ganador";

        lblCurrentPlayer.setText("¡FIN DEL JUEGO! Ganador: " + winnerName);
        lblCurrentPlayer.setStyle("-fx-text-fill: #FFD700; -fx-font-weight: bold; -fx-font-size: 16px;");

        showInformationAlert("¡Fin del Juego!", "El ganador es: " + winnerName + "\n\nTiempo total: " +
                formatTime(gameTimer.getGameDuration()));
    }

    /**
     * Formats time in seconds to MM:SS format.
     *
     * @param seconds the time in seconds
     * @return formatted time string
     */
    private String formatTime(long seconds) {
        long minutes = seconds / 60;
        long remainingSeconds = seconds % 60;
        return String.format("%02d:%02d", minutes, remainingSeconds);
    }

    /**
     * Shows an error alert dialog.
     *
     * @param title the title of the alert
     * @param message the message content
     */
    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Shows a warning alert dialog.
     *
     * @param title the title of the alert
     * @param message the message content
     */
    private void showWarningAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Shows an information alert dialog.
     *
     * @param title the title of the alert
     * @param message the message content
     */
    private void showInformationAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Updates the observer with changes from Observable objects.
     *
     * @param o the observable object
     * @param arg the argument passed by the observable
     */
    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof GameUnoModel && arg instanceof String) {
            String message = (String) arg;
            javafx.application.Platform.runLater(() -> {
                updateUI();
                lblGameStatus.setText(gameModel.getGameStatus());
            });
        } else if (o instanceof GameTimer && arg instanceof String) {
            String timerInfo = (String) arg;
            javafx.application.Platform.runLater(() -> {
                lblTimer.setText(timerInfo);
            });
        }
    }

    /**
     * Handles the back button action to show previous cards.
     *
     * @param event the action event
     */
    @FXML
    void onHandleBack(ActionEvent event) {
        if (gameModel != null && this.posInitCardToShow > 0) {
            this.posInitCardToShow--;
            printCardsHumanPlayer();
        }
    }

    /**
     * Handles the next button action to show next cards.
     *
     * @param event the action event
     */
    @FXML
    void onHandleNext(ActionEvent event) {
        if (gameModel != null && this.posInitCardToShow < this.gameModel.getHumanPlayer().getCardsPlayer().size() - 4) {
            this.posInitCardToShow++;
            printCardsHumanPlayer();
        }
    }

    /**
     * Handles the restart button action to restart the game.
     *
     * @param event the action event
     */
    @FXML
    void onHandleRestart(ActionEvent event) {
        if (gameModel == null) {
            showErrorAlert("Juego no inicializado", "El juego no se ha inicializado correctamente.");
            return;
        }

        try {
            if (gameTimer != null) gameTimer.stopTimer();
            for (ThreadMachinePlayer thread : machineThreads) {
                thread.stopThread();
            }

            initializeGame();
            showInformationAlert("Juego Reiniciado", "El juego ha sido reiniciado.");

        } catch (Exception e) {
            showErrorAlert("Error al reiniciar", e.getMessage());
        }
    }

    /**
     * Handles the exit button action to return to player selection screen.
     *
     * @param event the action event
     */
    @FXML
    void onHandleExit(ActionEvent event) {
        try {
            if (gameTimer != null) gameTimer.stopTimer();
            for (ThreadMachinePlayer thread : machineThreads) {
                thread.stopThread();
            }

            Stage currentStage = (Stage) lblTableSum.getScene().getWindow();
            currentStage.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/eiscuno/player-selection-view.fxml"));
            Parent root = loader.load();
            Stage selectionStage = new Stage();
            selectionStage.setTitle("Cincuentazo - Selección de Jugadores");
            selectionStage.setScene(new Scene(root));
            selectionStage.setResizable(false);
            selectionStage.show();

        } catch (Exception e) {
            showErrorAlert("Error al salir", e.getMessage());
        }
    }

    /**
     * Gets the game model.
     *
     * @return the current game model
     */
    public GameUnoModel getGameModel() {
        return gameModel;
    }

    /**
     * Ensures the current table card is always displayed.
     */
    private void ensureTableCardDisplayed() {
        if (gameModel == null || gameModel.getTable() == null) return;

        try {
            Card currentCard = gameModel.getTable().getCurrentCardOnTheTable();
            if (currentCard != null && tableImageView.getImage() == null) {
                tableImageView.setImage(currentCard.getImage());
            }
        } catch (Exception e) {
            // Table might be empty, which is normal
        }
    }

    /**
     * Inner class for managing game and turn timers.
     * Extends Observable to notify observers of time updates.
     */
    private class GameTimer extends Observable implements Runnable {
        private volatile boolean running;
        private long startTime;
        private long currentTime;
        private long turnStartTime;
        private String currentPlayer;

        /**
         * Constructs a new GameTimer with default values.
         */
        public GameTimer() {
            this.running = false;
            this.startTime = 0;
            this.currentTime = 0;
            this.turnStartTime = 0;
            this.currentPlayer = "DESCONOCIDO";
        }

        /**
         * Starts the game timer.
         */
        public void startTimer() {
            this.running = true;
            this.startTime = System.currentTimeMillis();
            this.turnStartTime = startTime;
            new Thread(this, "GameTimer").start();
        }

        /**
         * Stops the game timer.
         */
        public void stopTimer() {
            this.running = false;
        }

        /**
         * Starts or resets the turn timer for a specific player.
         *
         * @param playerName the name of the player whose turn is starting
         */
        public void startTurnTimer(String playerName) {
            this.turnStartTime = System.currentTimeMillis();
            this.currentPlayer = playerName;
            notifyObservers("Turno iniciado para: " + playerName);
        }

        /**
         * Main execution loop for the timer thread.
         */
        @Override
        public void run() {
            while (running) {
                try {
                    currentTime = System.currentTimeMillis();
                    updateDisplay();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println("Temporizador del juego interrumpido: " + e.getMessage());
                    break;
                }
            }
        }

        /**
         * Updates the timer display with current game and turn duration.
         */
        private void updateDisplay() {
            long gameDuration = (currentTime - startTime) / 1000;
            long turnDuration = (currentTime - turnStartTime) / 1000;

            String timerInfo = String.format(
                    "Tiempo: %02d:%02d | Turno: %02ds",
                    gameDuration / 60, gameDuration % 60, turnDuration
            );

            javafx.application.Platform.runLater(() -> {
                setChanged();
                notifyObservers(timerInfo);
            });
        }

        /**
         * Gets the total game duration in seconds.
         *
         * @return the game duration in seconds
         */
        public long getGameDuration() {
            return (currentTime - startTime) / 1000;
        }

        /**
         * Gets the current turn duration in seconds.
         *
         * @return the turn duration in seconds
         */
        public long getTurnDuration() {
            return (currentTime - turnStartTime) / 1000;
        }

        /**
         * Gets the name of the current player.
         *
         * @return the current player name
         */
        public String getCurrentPlayer() {
            return currentPlayer;
        }

        /**
         * Checks if the timer is running.
         *
         * @return true if the timer is running, false otherwise
         */
        public boolean isRunning() {
            return running;
        }

        /**
         * Notifies observers of timer updates.
         *
         * @param message the message to send to observers
         */
        private void notifyObservers(String message) {
            setChanged();
            super.notifyObservers(message);
        }
    }
}
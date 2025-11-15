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
 * Main controller for the Cincuentazo game
 * Manages game flow, UI updates and user interactions
 *
 * @author Jairo A. Tegue
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

    @FXML
    public void initialize() {
        this.posInitCardToShow = 0;
        this.machineThreads = new ArrayList<>();
        this.gameTimer = new GameTimer();
        this.gameTimer.addObserver(this);
        this.isHumanTurn = false;

        initializeUI();
    }

    private void initializeUI() {
        lblGameStatus.setText("Select a card to play...");
        lblTimer.setText("Time: 00:00");
        lblCardsRemaining.setText("52");
        lblCardsOnTable.setText("0");
        lblHumanCards.setText("Your cards: 0");
        lblActivePlayers.setText("Active players: 0");
        progressSum.setProgress(0.0);
    }

    public void setNumberOfMachinePlayers(int numberOfMachinePlayers) {
        this.numberOfMachinePlayers = numberOfMachinePlayers;
        initializeGame();
    }

    private void initializeGame() {
        try {
            Player humanPlayer = new Player("HUMAN_PLAYER");
            Deck deck = new Deck();
            Table table = new Table();

            this.gameModel = new GameUnoModel(humanPlayer, numberOfMachinePlayers, deck, table);
            this.gameModel.addObserver(this);
            this.gameModel.startGame();

            gameTimer.startTimer();

            // Set initial turn
            this.isHumanTurn = gameModel.getCurrentPlayer().getTypePlayer().startsWith("HUMAN");
            gameTimer.startTurnTimer("Human Player");

            updateUI();
            startMachineThreads();
            updateCurrentPlayerIndicator();

        } catch (Exception e) {
            showErrorAlert("Error initializing game", e.getMessage());
        }
    }

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

    private void updateUI() {
        printCardsHumanPlayer();
        updateTableInfo();
        updateMachinePlayersDisplay();
        updatePlayerInfo();
        updateProgressBar();
        lblGameStatus.setText(gameModel.getGameStatus());

        // Ensure table card is always displayed
        ensureTableCardDisplayed();

        if (gameModel != null && gameModel.getCurrentPlayer() != null) {
            this.isHumanTurn = gameModel.getCurrentPlayer().getTypePlayer().startsWith("HUMAN");
        }
    }

    private void printCardsHumanPlayer() {
        this.gridPaneCardsPlayer.getChildren().clear();

        if (gameModel.isPlayerEliminated(gameModel.getHumanPlayer())) {
            Label eliminatedLabel = new Label("❌ ELIMINATED - Cannot play more cards");
            eliminatedLabel.setStyle("-fx-text-fill: red; -fx-font-size: 16px; -fx-font-weight: bold;");
            this.gridPaneCardsPlayer.add(eliminatedLabel, 0, 0);
            return;
        }

        Card[] currentVisibleCards = this.gameModel.getCurrentVisibleCardsHumanPlayer(this.posInitCardToShow);

        for (int i = 0; i < currentVisibleCards.length; i++) {
            Card card = currentVisibleCards[i];
            ImageView cardImageView = card.getCard();

            boolean canPlayCard = card.canBePlayed(gameModel.getCurrentSum());

            // Only enable interaction if it's human turn and card can be played
            if (isHumanTurn && canPlayCard && !gameModel.isPlayerEliminated(gameModel.getHumanPlayer())) {
                cardImageView.setStyle("-fx-effect: dropshadow(gaussian, #00ff00, 15, 0.7, 0, 0); -fx-cursor: hand;");
                cardImageView.setOnMouseClicked((MouseEvent event) -> {
                    handleCardPlay(card);
                });

                // Add hover effects
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

    private void handleCardPlay(Card card) {
        try {
            if (gameModel == null) {
                showErrorAlert("Game not initialized", "The game has not been initialized correctly.");
                return;
            }

            if (!isHumanTurn) {
                showWarningAlert("Not your turn", "Wait for your turn to play a card.");
                return;
            }

            if (card.canBePlayed(gameModel.getCurrentSum())) {
                // Play the card
                gameModel.playCard(card, gameModel.getHumanPlayer());
                tableImageView.setImage(card.getImage());

                // Update UI immediately after playing card
                updateUI();

                // Take card from deck after playing (as per game rules)
                try {
                    gameModel.takeCardFromDeck(gameModel.getHumanPlayer());
                    updateUI(); // Update again after taking card

                    // Check if player is eliminated after taking card
                    if (!gameModel.canPlayerPlay(gameModel.getHumanPlayer())) {
                        showInformationAlert("Player Eliminated", "You cannot play any card. You are eliminated!");
                        gameModel.eliminatePlayer(gameModel.getHumanPlayer());
                        updateUI();
                    }

                } catch (Exception e) {
                    System.err.println("Error taking card after play: " + e.getMessage());
                }

                // Move to next turn only if player is not eliminated
                if (!gameModel.isPlayerEliminated(gameModel.getHumanPlayer())) {
                    gameModel.nextTurn();
                    updateCurrentPlayerIndicator();
                    gameTimer.startTurnTimer(gameModel.getCurrentPlayer().getTypePlayer());
                }

                // Check for game over
                if (gameModel.isGameOver()) {
                    handleGameOver();
                }

            } else {
                showWarningAlert("Invalid move", "This card would make the sum exceed 50.");
            }
        } catch (PlayerEliminatedException e) {
            showErrorAlert("Player Eliminated", e.getMessage());
            updateUI();
        } catch (InvalidCardException e) {
            showErrorAlert("Invalid Card", e.getMessage());
        } catch (Exception e) {
            showErrorAlert("Error playing card", e.getMessage());
        }
    }

    @FXML
    void onHandleTakeCard(ActionEvent event) {
        if (gameModel == null) {
            showErrorAlert("Game not initialized", "The game has not been initialized correctly.");
            return;
        }

        try {
            if (!isHumanTurn) {
                showWarningAlert("Not your turn", "You can only take cards on your turn.");
                return;
            }

            Card newCard = gameModel.takeCardFromDeck(gameModel.getHumanPlayer());
            printCardsHumanPlayer();
            showInformationAlert("Card Taken", "You have taken a card from the deck.");

            // Check if player is eliminated after taking card
            if (!gameModel.canPlayerPlay(gameModel.getHumanPlayer())) {
                showInformationAlert("Player Eliminated", "You cannot play any card. You are eliminated!");
                gameModel.eliminatePlayer(gameModel.getHumanPlayer());
                updateUI();
            } else {
                gameModel.nextTurn();
                updateCurrentPlayerIndicator();
            }

        } catch (Exception e) {
            showErrorAlert("Error taking card", e.getMessage());
        }
    }

    private void updateTableInfo() {
        if (gameModel == null) return;

        int currentSum = gameModel.getCurrentSum();
        lblTableSum.setText("Sum: " + currentSum + "/50");
        lblCardsOnTable.setText(String.valueOf(gameModel.getTable().getNumberOfCards()));
        lblCardsRemaining.setText(String.valueOf(gameModel.getDeck().size()));
    }

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

    private void updatePlayerInfo() {
        if (gameModel == null) return;

        lblHumanCards.setText("Your cards: " + gameModel.getHumanPlayer().getCardsPlayer().size());
        int activePlayers = countActivePlayers();
        lblActivePlayers.setText("Active players: " + activePlayers);
    }

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

    private void updateMachinePlayersDisplay() {
        machinePlayersContainer.getChildren().clear();

        if (gameModel == null) return;

        for (int i = 0; i < gameModel.getMachinePlayers().size(); i++) {
            Player machine = gameModel.getMachinePlayers().get(i);
            boolean isEliminated = gameModel.isPlayerEliminated(machine);

            String status;
            if (isEliminated) {
                status = "❌ ELIMINATED";
            } else if (machine.equals(gameModel.getCurrentPlayer())) {
                status = "🎯 TURN - " + machine.getCardsPlayer().size() + " cards";
            } else {
                status = "✅ " + machine.getCardsPlayer().size() + " cards";
            }

            Label machineLabel = new Label("Machine " + (i + 1) + ": " + status);

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

    private void updateCurrentPlayerIndicator() {
        if (gameModel == null) return;

        Player currentPlayer = gameModel.getCurrentPlayer();
        if (currentPlayer.getTypePlayer().startsWith("HUMAN")) {
            lblCurrentPlayer.setText("🎮 Turn: Human Player");
            lblCurrentPlayer.setStyle("-fx-text-fill: #4ECDC4; -fx-font-weight: bold; -fx-font-size: 14px;");
            this.isHumanTurn = true;
        } else {
            lblCurrentPlayer.setText("🤖 Turn: " + currentPlayer.getTypePlayer());
            lblCurrentPlayer.setStyle("-fx-text-fill: #FF6B6B; -fx-font-weight: bold; -fx-font-size: 14px;");
            this.isHumanTurn = false;
        }
    }

    private void handleGameOver() {
        for (ThreadMachinePlayer thread : machineThreads) {
            thread.stopThread();
        }

        gameTimer.stopTimer();

        Player winner = gameModel.determineWinner();
        String winnerName = winner != null ?
                (winner.getTypePlayer().startsWith("HUMAN") ? "🎉 Human Player!" : "🤖 " + winner.getTypePlayer()) :
                "No winner";

        lblCurrentPlayer.setText("GAME OVER! Winner: " + winnerName);
        lblCurrentPlayer.setStyle("-fx-text-fill: #FFD700; -fx-font-weight: bold; -fx-font-size: 16px;");

        showInformationAlert("Game Over!", "The winner is: " + winnerName + "\n\nTotal time: " +
                formatTime(gameTimer.getGameDuration()));
    }

    private String formatTime(long seconds) {
        long minutes = seconds / 60;
        long remainingSeconds = seconds % 60;
        return String.format("%02d:%02d", minutes, remainingSeconds);
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showWarningAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInformationAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

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

    @FXML
    void onHandleBack(ActionEvent event) {
        if (gameModel != null && this.posInitCardToShow > 0) {
            this.posInitCardToShow--;
            printCardsHumanPlayer();
        }
    }

    @FXML
    void onHandleNext(ActionEvent event) {
        if (gameModel != null && this.posInitCardToShow < this.gameModel.getHumanPlayer().getCardsPlayer().size() - 4) {
            this.posInitCardToShow++;
            printCardsHumanPlayer();
        }
    }


    @FXML
    void onHandleUno(ActionEvent event) {
        if (gameModel == null) {
            showErrorAlert("Game not initialized", "The game has not been initialized correctly.");
            return;
        }
        showInformationAlert("50ZO", "You shouted 50ZO! 🎯\nKeep it up!");
    }

    @FXML
    void onHandleRestart(ActionEvent event) {
        if (gameModel == null) {
            showErrorAlert("Game not initialized", "The game has not been initialized correctly.");
            return;
        }

        try {
            if (gameTimer != null) gameTimer.stopTimer();
            for (ThreadMachinePlayer thread : machineThreads) {
                thread.stopThread();
            }

            initializeGame();
            showInformationAlert("Game Restarted", "The game has been restarted.");

        } catch (Exception e) {
            showErrorAlert("Error restarting", e.getMessage());
        }
    }

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
            selectionStage.setTitle("Cincuentazo - Player Selection");
            selectionStage.setScene(new Scene(root));
            selectionStage.setResizable(false);
            selectionStage.show();

        } catch (Exception e) {
            showErrorAlert("Error exiting", e.getMessage());
        }
    }

    public GameUnoModel getGameModel() {
        return gameModel;
    }

    private class GameTimer extends Observable implements Runnable {
        private volatile boolean running;
        private long startTime;
        private long currentTime;
        private long turnStartTime;
        private String currentPlayer;

        public GameTimer() {
            this.running = false;
            this.startTime = 0;
            this.currentTime = 0;
            this.turnStartTime = 0;
            this.currentPlayer = "UNKNOWN";
        }

        public void startTimer() {
            this.running = true;
            this.startTime = System.currentTimeMillis();
            this.turnStartTime = startTime;
            new Thread(this, "GameTimer").start();
        }

        public void stopTimer() {
            this.running = false;
        }

        public void startTurnTimer(String playerName) {
            this.turnStartTime = System.currentTimeMillis();
            this.currentPlayer = playerName;
            notifyObservers("Turn started for: " + playerName);
        }

        @Override
        public void run() {
            while (running) {
                try {
                    currentTime = System.currentTimeMillis();
                    updateDisplay();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println("Game timer interrupted: " + e.getMessage());
                    break;
                }
            }
        }

        private void updateDisplay() {
            long gameDuration = (currentTime - startTime) / 1000;
            long turnDuration = (currentTime - turnStartTime) / 1000;

            String timerInfo = String.format(
                    "Time: %02d:%02d | Turn: %02ds",
                    gameDuration / 60, gameDuration % 60, turnDuration
            );

            javafx.application.Platform.runLater(() -> {
                setChanged();
                notifyObservers(timerInfo);
            });
        }

        public long getGameDuration() {
            return (currentTime - startTime) / 1000;
        }

        public long getTurnDuration() {
            return (currentTime - turnStartTime) / 1000;
        }

        public String getCurrentPlayer() {
            return currentPlayer;
        }

        public boolean isRunning() {
            return running;
        }

        private void notifyObservers(String message) {
            setChanged();
            super.notifyObservers(message);
        }
    }
    /**
     * Displays the initial card on the table
     */
    private void displayInitialCard() {
        if (gameModel == null || gameModel.getTable() == null) return;

        try {
            Card initialCard = gameModel.getTable().getCurrentCardOnTheTable();
            if (initialCard != null) {
                tableImageView.setImage(initialCard.getImage());
                System.out.println("Initial card displayed: " + initialCard.getValue());
            }
        } catch (Exception e) {
            System.err.println("Error displaying initial card: " + e.getMessage());
        }
    }

    /**
     * Ensures the current table card is always displayed
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
}
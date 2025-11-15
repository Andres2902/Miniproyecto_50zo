package org.example.eiscuno.model.machine;

import javafx.application.Platform;
import javafx.scene.image.ImageView;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.game.GameUnoModel;
import org.example.eiscuno.model.player.Player;

/**
 * Thread for managing machine player actions in Cincuentazo game.
 * Handles automated card playing, decision making, and turn management for AI players.
 *
 * @author Jairo Andrés Tegue
 * @version 1.0
 * @since 2025
 */
public class ThreadMachinePlayer extends Thread {
    private Player machinePlayer;
    private GameUnoModel game;
    private ImageView tableImageView;
    private volatile boolean running;
    private final int playerIndex;

    /**
     * Constructs a new ThreadMachinePlayer for a specific machine player.
     *
     * @param machinePlayer the machine player this thread controls
     * @param game the game model
     * @param tableImageView the ImageView displaying the current table card
     */
    public ThreadMachinePlayer(Player machinePlayer, GameUnoModel game, ImageView tableImageView) {
        this.machinePlayer = machinePlayer;
        this.game = game;
        this.tableImageView = tableImageView;
        this.running = true;
        this.playerIndex = game.getMachinePlayers().indexOf(machinePlayer) + 1;
        setName("MachinePlayer-" + playerIndex);
    }

    /**
     * Main execution loop for the machine player thread.
     * Continuously checks if it's this player's turn and performs actions accordingly.
     */
    @Override
    public void run() {
        while (running && !game.isGameOver()) {
            try {
                if (isMyTurn() && !game.isPlayerEliminated(machinePlayer)) {
                    performMachineTurn();
                }
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                break;
            } catch (Exception e) {
                System.err.println("Error in machine player thread: " + e.getMessage());
            }
        }
    }

    /**
     * Checks if it is currently this machine player's turn.
     *
     * @return true if it's this player's turn, false otherwise
     */
    private boolean isMyTurn() {
        Player currentPlayer = game.getCurrentPlayer();
        return currentPlayer != null && currentPlayer.equals(machinePlayer);
    }

    /**
     * Performs a complete turn for the machine player.
     * Includes thinking delay, card selection, playing, and drawing.
     */
    private void performMachineTurn() {
        try {
            Thread.sleep(2000 + (int)(Math.random() * 2000));

            Platform.runLater(() -> {
                try {
                    if (game.isPlayerEliminated(machinePlayer)) {
                        System.out.println("Machine " + playerIndex + " is already eliminated");
                        game.nextTurn();
                        return;
                    }

                    Card playableCard = game.findPlayableCard(machinePlayer);

                    if (playableCard != null) {
                        game.playCard(playableCard, machinePlayer);
                        tableImageView.setImage(playableCard.getImage());
                        System.out.println("Machine " + playerIndex + " played: " + playableCard.getValue());

                        game.takeCardFromDeck(machinePlayer);

                        if (!game.canPlayerPlay(machinePlayer)) {
                            System.out.println("Machine " + playerIndex + " eliminated - no playable cards");
                            game.eliminatePlayer(machinePlayer);
                        } else {
                            game.nextTurn();
                        }

                    } else {
                        game.takeCardFromDeck(machinePlayer);

                        if (!game.canPlayerPlay(machinePlayer)) {
                            System.out.println("Machine " + playerIndex + " eliminated - no playable cards");
                            game.eliminatePlayer(machinePlayer);
                        } else {
                            game.nextTurn();
                        }
                    }

                } catch (Exception e) {
                    System.err.println("Error in machine turn: " + e.getMessage());
                    try {
                        game.nextTurn();
                    } catch (Exception ex) {
                        System.err.println("Error advancing turn: " + ex.getMessage());
                    }
                }
            });

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Stops the thread execution gracefully.
     */
    public void stopThread() {
        running = false;
        interrupt();
    }

    /**
     * Checks if the thread is currently running.
     *
     * @return true if the thread is running, false otherwise
     */
    public boolean isRunning() {
        return running;
    }
}
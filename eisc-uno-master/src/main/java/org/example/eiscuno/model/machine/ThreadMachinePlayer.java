package org.example.eiscuno.model.machine;

import javafx.application.Platform;
import javafx.scene.image.ImageView;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.game.GameUnoModel;
import org.example.eiscuno.model.player.Player;

/**
 * Thread for managing machine player actions in Cincuentazo game
 *
 * @author Jairo A. Tegue
 * @version 1.0
 * @since 2025
 */
public class ThreadMachinePlayer extends Thread {
    private Player machinePlayer;
    private GameUnoModel game;
    private ImageView tableImageView;
    private volatile boolean running;
    private final int playerIndex;

    public ThreadMachinePlayer(Player machinePlayer, GameUnoModel game, ImageView tableImageView) {
        this.machinePlayer = machinePlayer;
        this.game = game;
        this.tableImageView = tableImageView;
        this.running = true;
        this.playerIndex = game.getMachinePlayers().indexOf(machinePlayer) + 1;
        setName("MachinePlayer-" + playerIndex);
    }

    @Override
    public void run() {
        while (running && !game.isGameOver()) {
            try {
                if (isMyTurn() && !game.isPlayerEliminated(machinePlayer)) {
                    performMachineTurn();
                }
                Thread.sleep(1000); // Check every second
            } catch (InterruptedException e) {
                break;
            } catch (Exception e) {
                System.err.println("Error in machine player thread: " + e.getMessage());
            }
        }
    }

    private boolean isMyTurn() {
        Player currentPlayer = game.getCurrentPlayer();
        return currentPlayer != null && currentPlayer.equals(machinePlayer);
    }

    private void performMachineTurn() {
        try {
            // Wait 2-4 seconds before playing
            Thread.sleep(2000 + (int)(Math.random() * 2000));

            Platform.runLater(() -> {
                try {
                    // Check if machine is eliminated before attempting to play
                    if (game.isPlayerEliminated(machinePlayer)) {
                        System.out.println("Machine " + playerIndex + " is already eliminated");
                        game.nextTurn();
                        return;
                    }

                    Card playableCard = game.findPlayableCard(machinePlayer);

                    if (playableCard != null) {
                        // Play card
                        game.playCard(playableCard, machinePlayer);
                        tableImageView.setImage(playableCard.getImage());
                        System.out.println("Machine " + playerIndex + " played: " + playableCard.getValue());

                        // Take card after playing
                        game.takeCardFromDeck(machinePlayer);

                        // Check if eliminated after taking card
                        if (!game.canPlayerPlay(machinePlayer)) {
                            System.out.println("Machine " + playerIndex + " eliminated - no playable cards");
                            game.eliminatePlayer(machinePlayer);
                        } else {
                            game.nextTurn();
                        }

                    } else {
                        // No playable cards - take card and check elimination
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
                        game.nextTurn(); // Ensure turn advances even on error
                    } catch (Exception ex) {
                        System.err.println("Error advancing turn: " + ex.getMessage());
                    }
                }
            });

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void stopThread() {
        running = false;
        interrupt();
    }

    public boolean isRunning() {
        return running;
    }
}
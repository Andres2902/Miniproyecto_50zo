package org.example.eiscuno.model.exceptions;

/**
 * Checked exception thrown when attempting to perform an operation on an eliminated player.
 * This exception indicates that a player has been removed from the game.
 *
 * @author Your Name
 * @version 1.0
 * @since 2024
 */
public class PlayerEliminatedException extends GameException {

    private final int cardsInHand;

    /**
     * Constructs a new PlayerEliminatedException with the specified player name.
     *
     * @param playerName the name of the eliminated player
     */
    public PlayerEliminatedException(String playerName) {
        super(
                "Player has been eliminated from the game",
                "PLAYER_ELIMINATED",
                playerName
        );
        this.cardsInHand = 0;
    }

    /**
     * Constructs a new PlayerEliminatedException with the specified player name and card count.
     *
     * @param playerName the name of the eliminated player
     * @param cardsInHand the number of cards the player had when eliminated
     */
    public PlayerEliminatedException(String playerName, int cardsInHand) {
        super(
                String.format("Player eliminated with %d cards in hand", cardsInHand),
                "PLAYER_ELIMINATED",
                playerName
        );
        this.cardsInHand = cardsInHand;
    }

    /**
     * Gets the number of cards the player had when eliminated.
     *
     * @return the number of cards
     */
    public int getCardsInHand() {
        return cardsInHand;
    }
}
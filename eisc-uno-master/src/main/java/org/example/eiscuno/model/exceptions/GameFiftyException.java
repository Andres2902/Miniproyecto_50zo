package org.example.eiscuno.model.exceptions;

/**
 * Checked exception thrown when a card play would exceed the maximum sum of 50.
 * This exception indicates an invalid move in the Cincuentazo game.
 *
 * @author Jairo Andrés Tegue
 * @version 1.0
 * @since 2025
 */
public class GameFiftyException extends GameException {

    private final int currentSum;
    private final int cardValue;

    /**
     * Constructs a new GameFiftyException with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception
     */
    public GameFiftyException(String message) {
        super(message, "EXCEEDS_FIFTY");
        this.currentSum = 0;
        this.cardValue = 0;
    }

    /**
     * Constructs a new GameFiftyException with a message detailing the sum violation.
     *
     * @param currentSum the current sum on the table
     * @param cardValue the value of the card that caused the violation
     */
    public GameFiftyException(int currentSum, int cardValue) {
        super(
                String.format("Cannot play card. Current sum: %d, Card value: %d, Total: %d (Limit: 50)",
                        currentSum, cardValue, currentSum + cardValue),
                "EXCEEDS_FIFTY",
                "CURRENT_PLAYER"
        );
        this.currentSum = currentSum;
        this.cardValue = cardValue;
    }

    /**
     * Gets the current sum when the exception occurred.
     *
     * @return the current sum on the table
     */
    public int getCurrentSum() {
        return currentSum;
    }

    /**
     * Gets the card value that caused the exception.
     *
     * @return the value of the card that would exceed the limit
     */
    public int getCardValue() {
        return cardValue;
    }
}
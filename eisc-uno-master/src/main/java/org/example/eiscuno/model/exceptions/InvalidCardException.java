package org.example.eiscuno.model.exceptions;

/**
 * Unchecked exception thrown when an invalid card operation is attempted.
 * This exception indicates programming errors or invalid game state.
 *
 * @author Jairo Andrés Tegue
 * @version 1.0
 * @since 2025
 */
public class InvalidCardException extends RuntimeException {

    private final String cardValue;
    private final String operation;

    /**
     * Constructs a new InvalidCardException with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception
     */
    public InvalidCardException(String message) {
        super(message);
        this.cardValue = "UNKNOWN";
        this.operation = "UNKNOWN";
    }

    /**
     * Constructs a new InvalidCardException with the specified detail message and card information.
     *
     * @param message the detail message explaining the reason for the exception
     * @param cardValue the value of the card that caused the exception
     * @param operation the operation being performed when the exception occurred
     */
    public InvalidCardException(String message, String cardValue, String operation) {
        super(message);
        this.cardValue = cardValue;
        this.operation = operation;
    }

    /**
     * Creates a new InvalidCardException for null card operations.
     *
     * @param operation the operation being performed when the exception occurred
     * @return a new InvalidCardException instance for null card error
     */
    public static InvalidCardException forNullCard(String operation) {
        return new InvalidCardException(
                "Cannot perform operation on null card",
                "NULL",
                operation
        );
    }

    /**
     * Creates a new InvalidCardException for invalid card values.
     *
     * @param cardValue the invalid card value
     * @param operation the operation being performed
     * @return a new InvalidCardException instance for invalid value error
     */
    public static InvalidCardException forInvalidValue(String cardValue, String operation) {
        return new InvalidCardException(
                "Card has invalid value: " + cardValue,
                cardValue,
                operation
        );
    }

    /**
     * Gets the card value that caused the exception.
     *
     * @return the card value
     */
    public String getCardValue() {
        return cardValue;
    }

    /**
     * Gets the operation being performed when the exception occurred.
     *
     * @return the operation name
     */
    public String getOperation() {
        return operation;
    }

    /**
     * Gets a detailed message including card information.
     *
     * @return the formatted error message with card and operation details
     */
    @Override
    public String getMessage() {
        return String.format("Card: %s, Operation: %s - %s", cardValue, operation, super.getMessage());
    }
}
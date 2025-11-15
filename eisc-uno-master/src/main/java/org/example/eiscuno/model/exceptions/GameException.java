package org.example.eiscuno.model.exceptions;

/**
 * Base exception class for all game-related exceptions in Cincuentazo.
 * Provides a common structure for game-specific error handling with error codes and player tracking.
 *
 * @author Jairo Andrés Tegue
 * @version 1.0
 * @since 2025
 */
public abstract class GameException extends Exception {

    private final String errorCode;
    private final String playerName;

    /**
     * Constructs a new GameException with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception
     */
    public GameException(String message) {
        super(message);
        this.errorCode = "GAME_ERROR";
        this.playerName = "UNKNOWN";
    }

    /**
     * Constructs a new GameException with the specified detail message and error code.
     *
     * @param message the detail message explaining the reason for the exception
     * @param errorCode the specific error code for this exception
     */
    public GameException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
        this.playerName = "UNKNOWN";
    }

    /**
     * Constructs a new GameException with the specified detail message, error code, and player name.
     *
     * @param message the detail message explaining the reason for the exception
     * @param errorCode the specific error code for this exception
     * @param playerName the name of the player involved in the error
     */
    public GameException(String message, String errorCode, String playerName) {
        super(message);
        this.errorCode = errorCode;
        this.playerName = playerName;
    }

    /**
     * Gets the error code associated with this exception.
     *
     * @return the error code
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * Gets the name of the player involved in the error.
     *
     * @return the player name
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Gets a detailed message including error code and player information.
     *
     * @return the formatted error message with code and player information
     */
    @Override
    public String getMessage() {
        return String.format("[%s] Player: %s - %s", errorCode, playerName, super.getMessage());
    }
}
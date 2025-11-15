package org.example.eiscuno.model.game;

import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.exceptions.GameFiftyException;

/**
 * Interface representing the Cincuentazo game functionality.
 * Defines the contract for game operations and state management.
 *
 * @author Your Name
 * @version 1.0
 * @since 2024
 */
public interface IGameUno {

    /**
     * Starts the Cincuentazo game with initial card distribution.
     */
    void startGame();

    /**
     * Makes a player draw a specified number of cards from the deck.
     *
     * @param player the player who will draw the cards
     * @param numberOfCards the number of cards to be drawn
     */
    void eatCard(Player player, int numberOfCards);

    /**
     * Plays a card in the game, adding it to the table.
     *
     * @param card the card to be played
     * @throws GameFiftyException if playing the card would exceed the maximum sum of 50
     */
    void playCard(Card card) throws GameFiftyException;

    /**
     * Handles the action when a player shouts "Uno" (maintained for compatibility).
     *
     * @param playerWhoSang the identifier of the player who shouted "Uno"
     */
    void haveSungOne(String playerWhoSang);

    /**
     * Retrieves the current visible cards of the human player for display.
     *
     * @param posInitCardToShow the starting position of the cards to be shown
     * @return an array of cards that are currently visible to the human player
     */
    Card[] getCurrentVisibleCardsHumanPlayer(int posInitCardToShow);

    /**
     * Checks if the game is over (only one player remains).
     *
     * @return true if the game is over, false otherwise
     */
    Boolean isGameOver();
}
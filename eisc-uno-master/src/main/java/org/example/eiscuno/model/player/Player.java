package org.example.eiscuno.model.player;

import org.example.eiscuno.model.card.Card;

import java.util.ArrayList;

/**
 * Represents a player in the Cincuentazo game.
 * Manages the player's hand of cards and player type identification.
 *
 * @author Jairo Andrés Tegue
 * @version 1.0
 * @since 2025
 */
public class Player implements IPlayer {
    private ArrayList<Card> cardsPlayer;
    private String typePlayer;

    /**
     * Constructs a new Player object with an empty hand of cards.
     *
     * @param typePlayer the type of player (e.g., "HUMAN_PLAYER", "MACHINE_PLAYER_1")
     */
    public Player(String typePlayer){
        this.cardsPlayer = new ArrayList<Card>();
        this.typePlayer = typePlayer;
    }

    /**
     * Adds a card to the player's hand.
     *
     * @param card the card to be added to the player's hand
     */
    @Override
    public void addCard(Card card){
        cardsPlayer.add(card);
    }

    /**
     * Retrieves all cards currently held by the player.
     *
     * @return an ArrayList containing all cards in the player's hand
     */
    @Override
    public ArrayList<Card> getCardsPlayer() {
        return cardsPlayer;
    }

    /**
     * Removes a card from the player's hand based on its index.
     *
     * @param index the index of the card to remove
     */
    @Override
    public void removeCard(int index) {
        cardsPlayer.remove(index);
    }

    /**
     * Retrieves a card from the player's hand based on its index.
     *
     * @param index the index of the card to retrieve
     * @return the card at the specified index in the player's hand
     */
    @Override
    public Card getCard(int index){
        return cardsPlayer.get(index);
    }

    /**
     * Gets the type of this player.
     *
     * @return the player type string
     */
    public String getTypePlayer() {
        return typePlayer;
    }
}
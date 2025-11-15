package org.example.eiscuno.model.table;

import org.example.eiscuno.model.card.Card;
import java.util.ArrayList;

/**
 * Represents the table in the Cincuentazo game where cards are played.
 * Manages the cards on the table and tracks the running sum.
 *
 * @author Jairo Andrés Tegue
 * @version 1.0
 * @since 2025
 */
public class Table {
    private ArrayList<Card> cardsTable;
    private int currentSum;

    /**
     * Constructs a new empty table with a sum of zero.
     */
    public Table(){
        this.cardsTable = new ArrayList<Card>();
        this.currentSum = 0;
    }

    /**
     * Adds a card to the table and updates the running sum.
     *
     * @param card the card to add to the table
     */
    public void addCardOnTheTable(Card card){
        this.cardsTable.add(card);
        updateSum(card);
    }

    /**
     * Updates the current sum based on the card's game value.
     * Ensures the sum never goes below zero.
     *
     * @param card the card whose value will be added to the sum
     */
    private void updateSum(Card card) {
        int cardValue = card.getGameValue(this.currentSum);
        this.currentSum += cardValue;

        if (this.currentSum < 0) {
            this.currentSum = 0;
        }
    }

    /**
     * Gets the current card on top of the table.
     *
     * @return the last card placed on the table, or null if table is empty
     */
    public Card getCurrentCardOnTheTable() {
        if (cardsTable.isEmpty()) {
            return null;
        }
        return this.cardsTable.get(this.cardsTable.size()-1);
    }

    /**
     * Gets the current sum of all cards on the table.
     *
     * @return the current sum
     */
    public int getCurrentSum() {
        return currentSum;
    }

    /**
     * Sets the current sum to a specific value.
     * Used for game state management.
     *
     * @param sum the new sum value
     */
    public void setCurrentSum(int sum) {
        this.currentSum = sum;
    }

    /**
     * Retrieves all cards except the last one for recycling into the deck.
     * Keeps only the top card on the table.
     *
     * @return an ArrayList containing all cards except the last one
     */
    public ArrayList<Card> getAllCardsExceptLast() {
        if (cardsTable.size() <= 1) {
            return new ArrayList<>();
        }
        ArrayList<Card> recycledCards = new ArrayList<>(cardsTable.subList(0, cardsTable.size() - 1));
        Card lastCard = cardsTable.get(cardsTable.size() - 1);
        cardsTable.clear();
        cardsTable.add(lastCard);
        return recycledCards;
    }

    /**
     * Checks if the table is empty.
     *
     * @return true if no cards are on the table, false otherwise
     */
    public boolean isEmpty() {
        return cardsTable.isEmpty();
    }

    /**
     * Gets the total number of cards currently on the table.
     *
     * @return the number of cards on the table
     */
    public int getNumberOfCards() {
        return cardsTable.size();
    }
}
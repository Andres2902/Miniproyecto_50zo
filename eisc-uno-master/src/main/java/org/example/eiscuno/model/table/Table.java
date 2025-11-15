package org.example.eiscuno.model.table;

import org.example.eiscuno.model.card.Card;
import java.util.ArrayList;

/**
 * Represents the table in the Cincuentazo game where cards are played
 *
 * @author Jairo A. Tegue
 * @version 1.0
 * @since 2025
 */
public class Table {
    private ArrayList<Card> cardsTable;
    private int currentSum;

    public Table(){
        this.cardsTable = new ArrayList<Card>();
        this.currentSum = 0;
    }

    public void addCardOnTheTable(Card card){
        this.cardsTable.add(card);
        updateSum(card);
    }

    private void updateSum(Card card) {
        int cardValue = card.getGameValue(this.currentSum);
        this.currentSum += cardValue;

        // Ensure sum doesn't go below zero
        if (this.currentSum < 0) {
            this.currentSum = 0;
        }
    }

    public Card getCurrentCardOnTheTable() {
        if (cardsTable.isEmpty()) {
            // Return null instead of throwing exception for better handling
            return null;
        }
        return this.cardsTable.get(this.cardsTable.size()-1);
    }

    public int getCurrentSum() {
        return currentSum;
    }

    public void setCurrentSum(int sum) {
        this.currentSum = sum;
    }

    public ArrayList<Card> getAllCardsExceptLast() {
        if (cardsTable.size() <= 1) {
            return new ArrayList<>();
        }
        ArrayList<Card> recycledCards = new ArrayList<>(cardsTable.subList(0, cardsTable.size() - 1));
        Card lastCard = cardsTable.get(cardsTable.size() - 1);
        cardsTable.clear();
        cardsTable.add(lastCard); // Keep only the last played card
        return recycledCards;
    }

    public boolean isEmpty() {
        return cardsTable.isEmpty();
    }

    public int getNumberOfCards() {
        return cardsTable.size();
    }
}
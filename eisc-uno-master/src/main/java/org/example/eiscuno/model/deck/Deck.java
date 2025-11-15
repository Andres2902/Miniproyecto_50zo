package org.example.eiscuno.model.deck;

import org.example.eiscuno.model.unoenum.EISCUnoEnum;
import org.example.eiscuno.model.card.Card;

import java.util.Collections;
import java.util.Stack;

/**
 * Represents a deck of playing cards for Cincuentazo game.
 */
public class Deck {
    private Stack<Card> deckOfCards;

    /**
     * Constructs a new deck of playing cards and initializes it.
     */
    public Deck() {
        deckOfCards = new Stack<>();
        initializeDeck();
    }

    /**
     * Initializes the deck with cards based on the EISCUnoEnum values.
     */
    private void initializeDeck() {
        System.out.println("Initializing Cincuentazo deck...");

        for (EISCUnoEnum cardEnum : EISCUnoEnum.values()) {
            // Include only playing cards (hearts, diamonds, spades, clubs)
            if (cardEnum.name().startsWith("COR_") ||
                    cardEnum.name().startsWith("DIAM_") ||
                    cardEnum.name().startsWith("PIC_") ||
                    cardEnum.name().startsWith("TREB_")){

                String cardValue = getCardValue(cardEnum.name());
                String cardColor = getCardColor(cardEnum.name());

                System.out.println("Creating card: " + cardEnum.name() + " -> " + cardValue + " of " + cardColor);

                try {
                    Card card = new Card(cardEnum.getFilePath(), cardValue, cardColor);
                    deckOfCards.push(card);
                } catch (Exception e) {
                    System.err.println("Error creating card " + cardEnum.name() + ": " + e.getMessage());
                }
            }
        }

        Collections.shuffle(deckOfCards);
        System.out.println("Deck initialized with " + deckOfCards.size() + " cards");
    }

    /**
     * Extracts the card value from the enum name.
     */
    private String getCardValue(String name) {
        if (name.contains("2")) return "2";
        if (name.contains("3")) return "3";
        if (name.contains("4")) return "4";
        if (name.contains("5")) return "5";
        if (name.contains("6")) return "6";
        if (name.contains("7")) return "7";
        if (name.contains("8")) return "8";
        if (name.contains("9")) return "9";
        if (name.contains("10")) return "10";
        if (name.contains("J")) return "J";
        if (name.contains("Q")) return "Q";
        if (name.contains("K")) return "K";
        if (name.contains("AS")) return "A";

        // For deck and card back, return a default value
        if (name.equals("DECK_OF_CARDS") || name.equals("CARD_BACK")) {
            return "0";
        }

        return null;
    }

    /**
     * Extracts the card color/suit from the enum name.
     */
    private String getCardColor(String name) {
        if (name.startsWith("COR_")) {
            return "HEARTS";
        } else if (name.startsWith("DIAM_")) {
            return "DIAMONDS";
        } else if (name.startsWith("PIC_")) {
            return "SPADES";
        } else if (name.startsWith("TREB_")) {
            return "CLUBS";
        } else if (name.equals("DECK_OF_CARDS") || name.equals("CARD_BACK")) {
            return "BACK";
        } else {
            return null;
        }
    }

    /**
     * Takes a card from the top of the deck.
     *
     * @return the card from the top of the deck
     * @throws IllegalStateException if the deck is empty
     */
    public Card takeCard() {
        if (deckOfCards.isEmpty()) {
            throw new IllegalStateException("No hay más cartas en el mazo.");
        }
        return deckOfCards.pop();
    }

    /**
     * Checks if the deck is empty.
     *
     * @return true if the deck is empty, false otherwise
     */
    public boolean isEmpty() {
        return deckOfCards.isEmpty();
    }

    /**
     * Gets the number of cards in the deck.
     *
     * @return the number of cards remaining
     */
    public int size() {
        return deckOfCards.size();
    }

    /**
     * Adds cards to the bottom of the deck.
     *
     * @param cards the cards to add
     */
    public void addCards(Stack<Card> cards) {
        deckOfCards.addAll(0, cards);
    }

    /**
     * Takes all cards from the deck except the last one for recycling.
     *
     * @return a stack containing all cards except the last one
     */
    public Stack<Card> takeAllExceptLast() {
        Stack<Card> recycledCards = new Stack<>();
        // Keep the last card in deck, take all others for recycling
        while (deckOfCards.size() > 1) {
            recycledCards.push(deckOfCards.pop());
        }
        return recycledCards;
    }

    /**
     * Recycles cards by adding them to the deck and shuffling.
     * Used when the deck needs to be replenished during gameplay.
     *
     * @param cards the cards to recycle into the deck
     */
    public void recycleCards(Stack<Card> cards) {
        addCards(cards);
        Collections.shuffle(deckOfCards);
        System.out.println("Deck recycled with " + cards.size() + " cards. New size: " + deckOfCards.size());
    }
}
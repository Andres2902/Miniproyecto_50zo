package org.example.eiscuno.model.card;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Represents a playing card in the Cincuentazo game.
 * Each card has an image, value, color, and game-specific behavior.
 *
 * @author Jairo Andrés Tegue
 * @version 1.0
 * @since 2025
 */
public class Card {
    private String url;
    private String value;
    private String color;
    private Image image;
    private ImageView cardImageView;

    /**
     * Constructs a Card with the specified image URL, value, and color.
     *
     * @param url the path to the card image resource
     * @param value the value of the card (2-10, J, Q, K, A)
     * @param color the suit/color of the card (HEARTS, DIAMONDS, SPADES, CLUBS)
     */
    public Card(String url, String value, String color) {
        this.url = url;
        this.value = value;
        this.color = color;
        this.image = new Image(String.valueOf(getClass().getResource(url)));
        this.cardImageView = createCardImageView();
    }

    /**
     * Creates and configures the ImageView for the card display.
     *
     * @return a configured ImageView for this card
     */
    private ImageView createCardImageView() {
        ImageView card = new ImageView(this.image);
        card.setY(16);
        card.setFitHeight(90);
        card.setFitWidth(70);
        return card;
    }

    /**
     * Gets the game value of this card according to Cincuentazo rules.
     * Uses CardValueCalculator to determine the optimal value based on current table sum.
     *
     * @param currentTableSum the current sum of cards on the table
     * @return the calculated value of this card
     */
    public int getGameValue(int currentTableSum) {
        if ("A".equals(this.value)) {
            return CardValueCalculator.calculateOptimalAValue(currentTableSum);
        }
        return CardValueCalculator.calculateValue(this.value);
    }

    /**
     * Checks if this card can be played without exceeding the maximum sum of 50.
     *
     * @param currentTableSum the current sum of cards on the table
     * @return true if the card can be played, false otherwise
     */
    public boolean canBePlayed(int currentTableSum) {
        return CardValueCalculator.isValidPlay(this.value, currentTableSum);
    }

    /**
     * Gets the ImageView representation of this card for GUI display.
     *
     * @return the ImageView of this card
     */
    public ImageView getCard() {
        return cardImageView;
    }

    /**
     * Gets the Image object of this card.
     *
     * @return the Image of this card
     */
    public Image getImage() {
        return image;
    }

    /**
     * Gets the string value of this card.
     *
     * @return the value of this card (2-10, J, Q, K, A)
     */
    public String getValue() {
        return value;
    }

    /**
     * Gets the color/suit of this card.
     *
     * @return the color/suit of this card
     */
    public String getColor() {
        return color;
    }

    /**
     * Gets the URL path to the card image resource.
     *
     * @return the URL path to the card image
     */
    public String getUrl() {
        return url;
    }

    /**
     * Returns a string representation of the card for debugging purposes.
     *
     * @return a string containing the card's value and color
     */
    @Override
    public String toString() {
        return String.format("Card{value='%s', color='%s'}", value, color);
    }
}
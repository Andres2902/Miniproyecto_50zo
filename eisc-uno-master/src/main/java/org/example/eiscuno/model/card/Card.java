package org.example.eiscuno.model.card;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Represents a playing card in the Cincuentazo game
 * Each card has an image, value, and game-specific behavior
 *
 * @author Jairo A. Tegue
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
     * Constructs a Card with the specified image URL and value
     */
    public Card(String url, String value, String color) {
        this.url = url;
        this.value = value;
        this.color = color;
        this.image = new Image(String.valueOf(getClass().getResource(url)));
        this.cardImageView = createCardImageView();
    }

    /**
     * Creates and configures the ImageView for the card display
     */
    private ImageView createCardImageView() {
        ImageView card = new ImageView(this.image);
        card.setY(16);
        card.setFitHeight(90);
        card.setFitWidth(70);
        return card;
    }

    /**
     * Gets the game value of this card according to Cincuentazo rules
     */
    public int getGameValue(int currentTableSum) {
        if ("A".equals(this.value)) {
            return CardValueCalculator.calculateOptimalAValue(currentTableSum);
        }
        return CardValueCalculator.calculateValue(this.value);
    }

    /**
     * Checks if this card can be played without exceeding the maximum sum of 50
     */
    public boolean canBePlayed(int currentTableSum) {
        return CardValueCalculator.isValidPlay(this.value, currentTableSum);
    }

    /**
     * Gets the ImageView representation of this card for GUI display
     */
    public ImageView getCard() {
        return cardImageView;
    }

    /**
     * Gets the Image object of this card
     */
    public Image getImage() {
        return image;
    }

    /**
     * Gets the string value of this card
     */
    public String getValue() {
        return value;
    }

    /**
     * Gets the color/suit of this card
     */
    public String getColor() {
        return color;
    }

    /**
     * Gets the URL path to the card image resource
     */
    public String getUrl() {
        return url;
    }

    /**
     * Returns a string representation of the card for debugging
     */
    @Override
    public String toString() {
        return String.format("Card{value='%s', color='%s'}", value, color);
    }
}
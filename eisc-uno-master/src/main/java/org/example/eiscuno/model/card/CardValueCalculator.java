package org.example.eiscuno.model.card;

/**
 * Utility class for calculating card values according to Cincuentazo game rules.
 * Handles special card values and validation for game moves.
 *
 * @author Jairo Andrés Tegue
 * @version 1.0
 * @since 2025
 */
public class CardValueCalculator {

    /**
     * Calculates the numeric value of a card according to Cincuentazo rules.
     * - Number cards (2-8, 10): face value
     * - 9: 0 points
     * - J, Q, K: -10 points
     * - A: 1 point (base value, can be 10 with optimal calculation)
     *
     * @param cardValue the string value of the card
     * @return the calculated numeric value
     */
    public static int calculateValue(String cardValue) {
        if (cardValue == null) return 0;

        switch (cardValue) {
            case "2": case "3": case "4": case "5":
            case "6": case "7": case "8": case "10":
                return Integer.parseInt(cardValue);

            case "9":
                return 0;

            case "J": case "Q": case "K":
                return -10;

            case "A":
                return 1;

            default:
                return 0;
        }
    }

    /**
     * Calculates the optimal value for an Ace card (1 or 10) based on the current sum.
     * The Ace will be valued at 10 if it doesn't cause the sum to exceed 50,
     * otherwise it will be valued at 1.
     *
     * @param currentSum the current sum on the table
     * @return 10 if currentSum + 10 <= 50, otherwise 1
     */
    public static int calculateOptimalAValue(int currentSum) {
        if (currentSum <= 40) {
            return 10;
        } else if (currentSum <= 49) {
            return 1;
        } else {
            return 1;
        }
    }

    /**
     * Validates if a card can be played without exceeding the maximum sum of 50.
     *
     * @param cardValue the value of the card to validate
     * @param currentSum the current sum on the table
     * @return true if playing the card won't exceed 50, false otherwise
     */
    public static boolean isValidPlay(String cardValue, int currentSum) {
        int value = calculateValue(cardValue);
        if ("A".equals(cardValue)) {
            return (currentSum + 1 <= 50) || (currentSum + 10 <= 50);
        }
        return currentSum + value <= 50;
    }
}
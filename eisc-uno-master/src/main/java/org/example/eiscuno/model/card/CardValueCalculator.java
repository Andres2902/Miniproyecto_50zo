package org.example.eiscuno.model.card;

/**
 * Utility class for calculating card values according to Cincuentazo game rules
 *
 * @author Jairo A. Tegue
 * @version 1.0
 * @since 2025
 */
public class CardValueCalculator {

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

    public static int calculateOptimalAValue(int currentSum) {
        if (currentSum <= 40) {
            return 10;
        } else if (currentSum <= 49) {
            return 1;
        } else {
            return 1;
        }
    }

    public static boolean isValidPlay(String cardValue, int currentSum) {
        int value = calculateValue(cardValue);
        if ("A".equals(cardValue)) {
            return (currentSum + 1 <= 50) || (currentSum + 10 <= 50);
        }
        return currentSum + value <= 50;
    }
}
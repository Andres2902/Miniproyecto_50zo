package org.example.eiscuno.model.game;

import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;
import org.example.eiscuno.model.exceptions.PlayerEliminatedException;
import org.example.eiscuno.model.exceptions.InvalidCardException;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Main model class for the Cincuentazo game following MVC pattern
 *
 * @author Jairo A. Tegue
 * @version 1.0
 * @since 2025
 */
public class GameUnoModel extends Observable {

    private Player humanPlayer;
    private List<Player> machinePlayers;
    private Deck deck;
    private Table table;
    private int currentPlayerIndex;
    private boolean gameOver;
    private int numberOfMachinePlayers;
    private String gameStatus;
    private List<Player> eliminatedPlayers; // Track eliminated players separately

    public GameUnoModel(Player humanPlayer, int numberOfMachinePlayers, Deck deck, Table table) {
        this.humanPlayer = humanPlayer;
        this.numberOfMachinePlayers = numberOfMachinePlayers;
        this.machinePlayers = new ArrayList<>();
        this.deck = deck;
        this.table = table;
        this.currentPlayerIndex = 0;
        this.gameOver = false;
        this.gameStatus = "Game initialized";
        this.eliminatedPlayers = new ArrayList<>();

        initializeMachinePlayers();
    }

    private void initializeMachinePlayers() {
        for (int i = 0; i < numberOfMachinePlayers; i++) {
            machinePlayers.add(new Player("MACHINE_PLAYER_" + (i + 1)));
        }
        notifyObservers("Machine players initialized: " + numberOfMachinePlayers);
    }

    public void startGame() {
        // Deal 4 cards to each player
        for (int i = 0; i < 4; i++) {
            humanPlayer.addCard(this.deck.takeCard());
            for (Player machine : machinePlayers) {
                machine.addCard(this.deck.takeCard());
            }
        }

        // Place initial card on the table
        if (!deck.isEmpty()) {
            Card initialCard = deck.takeCard();
            table.addCardOnTheTable(initialCard);

            // Notificar a los observadores sobre la carta inicial
            gameStatus = "Initial card: " + initialCard.getValue();
            notifyObservers("INITIAL_CARD:" + initialCard.getValue());
        }

        gameStatus = "Game started";
        notifyObservers("Game started with " + (numberOfMachinePlayers + 1) + " players");
    }

    public void playCard(Card card, Player player) throws PlayerEliminatedException, InvalidCardException {
        validatePlayerAction(player);

        if (card == null) {
            throw new InvalidCardException("Card cannot be null", "NULL", "playCard");
        }

        if (!card.canBePlayed(table.getCurrentSum())) {
            throw new InvalidCardException(
                    "Cannot play this card. Current sum: " + table.getCurrentSum(),
                    card.getValue(),
                    "playCard"
            );
        }

        // Play the card
        this.table.addCardOnTheTable(card);

        // Remove card from player's hand
        int cardIndex = findCardIndex(player, card);
        if (cardIndex != -1) {
            player.removeCard(cardIndex);
        }

        gameStatus = player.getTypePlayer() + " played " + card.getValue();
        notifyObservers("Card played: " + card.getValue() + ", New sum: " + table.getCurrentSum());
    }

    public Card takeCardFromDeck(Player player) throws PlayerEliminatedException {
        validatePlayerAction(player);

        if (deck.isEmpty()) {
            recycleDeck();
        }

        Card newCard = deck.takeCard();
        player.addCard(newCard);

        notifyObservers(player.getTypePlayer() + " took a card from deck");
        return newCard;
    }

    private void validatePlayerAction(Player player) throws PlayerEliminatedException {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null");
        }

        if (isPlayerEliminated(player)) {
            throw new PlayerEliminatedException(player.getTypePlayer());
        }

        if (!player.equals(getCurrentPlayer())) {
            throw new IllegalArgumentException("It's not " + player.getTypePlayer() + "'s turn");
        }
    }

    private void recycleDeck() {
        ArrayList<Card> recycledCards = table.getAllCardsExceptLast();
        if (!recycledCards.isEmpty()) {
            java.util.Stack<Card> cardStack = new java.util.Stack<>();
            cardStack.addAll(recycledCards);
            deck.recycleCards(cardStack);
            notifyObservers("Deck recycled with " + recycledCards.size() + " cards");
        }
    }

    /**
     * Finds a playable card for a player without causing recursion
     */
    public Card findPlayableCard(Player player) {
        // Check if player is in eliminated list first
        if (eliminatedPlayers.contains(player)) {
            return null;
        }

        // Check if player has cards
        if (player.getCardsPlayer().isEmpty()) {
            return null;
        }

        // Find playable card
        for (Card card : player.getCardsPlayer()) {
            if (card.canBePlayed(table.getCurrentSum())) {
                return card;
            }
        }
        return null;
    }

    /**
     * Checks if player can play any card without recursion
     */
    public boolean canPlayerPlay(Player player) {
        // Simple check - if player is in eliminated list, they cannot play
        if (eliminatedPlayers.contains(player)) {
            return false;
        }

        // Direct check for playable cards
        for (Card card : player.getCardsPlayer()) {
            if (card.canBePlayed(table.getCurrentSum())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Eliminates a player from the game
     */
    public void eliminatePlayer(Player player) {
        if (eliminatedPlayers.contains(player)) {
            return; // Already eliminated
        }

        int cardsCount = player.getCardsPlayer().size();

        // Send eliminated player's cards to the deck
        java.util.Stack<Card> cardStack = new java.util.Stack<>();
        cardStack.addAll(player.getCardsPlayer());
        deck.addCards(cardStack);

        // Clear player's hand
        player.getCardsPlayer().clear();

        // Add to eliminated players list
        eliminatedPlayers.add(player);

        gameStatus = player.getTypePlayer() + " eliminated with " + cardsCount + " cards. Sum: " + table.getCurrentSum();
        notifyObservers("Player eliminated: " + player.getTypePlayer() + ". Current sum: " + table.getCurrentSum());

        // If eliminated player was current, move to next turn
        if (player.equals(getCurrentPlayer())) {
            nextTurn();
        }
    }

    public void nextTurn() {
        int totalPlayers = getTotalPlayers();
        int attempts = 0;
        Player nextPlayer;

        do {
            currentPlayerIndex = (currentPlayerIndex + 1) % totalPlayers;
            nextPlayer = getCurrentPlayer();
            attempts++;

            if (attempts >= totalPlayers * 2) {
                // No active players found
                gameOver = true;
                gameStatus = "Game Over - No active players";
                notifyObservers("Game Over - No active players");
                return;
            }
        } while (isPlayerEliminated(nextPlayer));

        gameStatus = "Turn: " + getCurrentPlayer().getTypePlayer();
        notifyObservers("Turn changed to: " + getCurrentPlayer().getTypePlayer());
    }

    /**
     * Checks if player is eliminated - uses the eliminated players list
     */
    public boolean isPlayerEliminated(Player player) {
        return eliminatedPlayers.contains(player);
    }

    public Boolean isGameOver() {
        int activePlayers = countActivePlayers();

        boolean over = activePlayers <= 1;
        if (over && !gameOver) {
            gameOver = true;
            Player winner = determineWinner();
            if (winner != null) {
                gameStatus = "Game Over - Winner: " + winner.getTypePlayer();
                notifyObservers("Game Over - Winner: " + winner.getTypePlayer());
            } else {
                gameStatus = "Game Over - No winners";
                notifyObservers("Game Over - No winners");
            }
        }

        return over;
    }

    private int countActivePlayers() {
        int activePlayers = 0;

        if (!isPlayerEliminated(humanPlayer)) {
            activePlayers++;
        }

        for (Player machine : machinePlayers) {
            if (!isPlayerEliminated(machine)) {
                activePlayers++;
            }
        }

        return activePlayers;
    }

    public Player determineWinner() {
        if (!isPlayerEliminated(humanPlayer)) {
            return humanPlayer;
        }

        for (Player machine : machinePlayers) {
            if (!isPlayerEliminated(machine)) {
                return machine;
            }
        }

        return null;
    }

    private void notifyObservers(String message) {
        setChanged();
        super.notifyObservers(message);
    }

    private int findCardIndex(Player player, Card card) {
        for (int i = 0; i < player.getCardsPlayer().size(); i++) {
            if (player.getCardsPlayer().get(i).equals(card)) {
                return i;
            }
        }
        return -1;
    }

    // Getters
    public int getCurrentSum() {
        return table.getCurrentSum();
    }

    public List<Player> getMachinePlayers() {
        return machinePlayers;
    }

    public Player getCurrentPlayer() {
        if (currentPlayerIndex == 0) {
            return humanPlayer;
        } else {
            return machinePlayers.get(currentPlayerIndex - 1);
        }
    }

    public int getTotalPlayers() {
        return machinePlayers.size() + 1;
    }

    public Player getHumanPlayer() {
        return humanPlayer;
    }

    public Table getTable() {
        return table;
    }

    public Deck getDeck() {
        return deck;
    }

    public String getGameStatus() {
        return gameStatus;
    }

    public Card[] getCurrentVisibleCardsHumanPlayer(int posInitCardToShow) {
        int totalCards = this.humanPlayer.getCardsPlayer().size();
        int numVisibleCards = Math.min(4, totalCards - posInitCardToShow);
        Card[] cards = new Card[numVisibleCards];

        for (int i = 0; i < numVisibleCards; i++) {
            cards[i] = this.humanPlayer.getCard(posInitCardToShow + i);
        }

        return cards;
    }
}
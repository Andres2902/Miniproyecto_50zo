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
 * Main model class for the Cincuentazo game following MVC pattern.
 * Manages game state, player turns, card playing, and game rules enforcement.
 *
 * @author Jairo Andrés Tegue
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
    private List<Player> eliminatedPlayers;

    /**
     * Constructs a new GameUnoModel with the specified players, deck, and table.
     *
     * @param humanPlayer the human player
     * @param numberOfMachinePlayers the number of machine players (1-3)
     * @param deck the deck of cards
     * @param table the game table
     */
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

    /**
     * Initializes the machine players for the game.
     */
    private void initializeMachinePlayers() {
        for (int i = 0; i < numberOfMachinePlayers; i++) {
            machinePlayers.add(new Player("MACHINE_PLAYER_" + (i + 1)));
        }
        notifyObservers("Machine players initialized: " + numberOfMachinePlayers);
    }

    /**
     * Starts the game by dealing initial cards and placing the first card on the table.
     */
    public void startGame() {
        for (int i = 0; i < 4; i++) {
            humanPlayer.addCard(this.deck.takeCard());
            for (Player machine : machinePlayers) {
                machine.addCard(this.deck.takeCard());
            }
        }

        if (!deck.isEmpty()) {
            Card initialCard = deck.takeCard();
            table.addCardOnTheTable(initialCard);

            gameStatus = "Initial card: " + initialCard.getValue();
            notifyObservers("INITIAL_CARD:" + initialCard.getValue());
        }

        gameStatus = "Game started";
        notifyObservers("Game started with " + (numberOfMachinePlayers + 1) + " players");
    }

    /**
     * Plays a card from a player's hand onto the table.
     *
     * @param card the card to play
     * @param player the player playing the card
     * @throws PlayerEliminatedException if the player has been eliminated
     * @throws InvalidCardException if the card is null or cannot be played
     */
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

        this.table.addCardOnTheTable(card);

        int cardIndex = findCardIndex(player, card);
        if (cardIndex != -1) {
            player.removeCard(cardIndex);
        }

        gameStatus = player.getTypePlayer() + " played " + card.getValue();
        notifyObservers("Card played: " + card.getValue() + ", New sum: " + table.getCurrentSum());
    }

    /**
     * Makes a player take a card from the deck.
     * If the deck is empty, it recycles cards from the table.
     *
     * @param player the player taking a card
     * @return the card taken from the deck
     * @throws PlayerEliminatedException if the player has been eliminated
     */
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

    /**
     * Validates that a player can perform an action (is current player and not eliminated).
     *
     * @param player the player to validate
     * @throws PlayerEliminatedException if the player has been eliminated
     * @throws IllegalArgumentException if the player is null or not the current player
     */
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

    /**
     * Recycles cards from the table back into the deck when the deck is empty.
     */
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
     * Finds a playable card for a player from their hand.
     *
     * @param player the player to find a card for
     * @return a playable card, or null if none available
     */
    public Card findPlayableCard(Player player) {
        if (eliminatedPlayers.contains(player)) {
            return null;
        }

        if (player.getCardsPlayer().isEmpty()) {
            return null;
        }

        for (Card card : player.getCardsPlayer()) {
            if (card.canBePlayed(table.getCurrentSum())) {
                return card;
            }
        }
        return null;
    }

    /**
     * Checks if a player can play any card from their hand.
     *
     * @param player the player to check
     * @return true if the player has at least one playable card, false otherwise
     */
    public boolean canPlayerPlay(Player player) {
        if (eliminatedPlayers.contains(player)) {
            return false;
        }

        for (Card card : player.getCardsPlayer()) {
            if (card.canBePlayed(table.getCurrentSum())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Eliminates a player from the game.
     * Sends the player's cards back to the deck and adds them to the eliminated players list.
     *
     * @param player the player to eliminate
     */
    public void eliminatePlayer(Player player) {
        if (eliminatedPlayers.contains(player)) {
            return;
        }

        int cardsCount = player.getCardsPlayer().size();

        java.util.Stack<Card> cardStack = new java.util.Stack<>();
        cardStack.addAll(player.getCardsPlayer());
        deck.addCards(cardStack);

        player.getCardsPlayer().clear();

        eliminatedPlayers.add(player);

        gameStatus = player.getTypePlayer() + " eliminated with " + cardsCount + " cards. Sum: " + table.getCurrentSum();
        notifyObservers("Player eliminated: " + player.getTypePlayer() + ". Current sum: " + table.getCurrentSum());

        if (player.equals(getCurrentPlayer())) {
            nextTurn();
        }
    }

    /**
     * Advances the game to the next player's turn.
     * Skips eliminated players automatically.
     */
    public void nextTurn() {
        int totalPlayers = getTotalPlayers();
        int attempts = 0;
        Player nextPlayer;

        do {
            currentPlayerIndex = (currentPlayerIndex + 1) % totalPlayers;
            nextPlayer = getCurrentPlayer();
            attempts++;

            if (attempts >= totalPlayers * 2) {
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
     * Checks if a player has been eliminated from the game.
     *
     * @param player the player to check
     * @return true if the player is eliminated, false otherwise
     */
    public boolean isPlayerEliminated(Player player) {
        return eliminatedPlayers.contains(player);
    }

    /**
     * Checks if the game is over (only one or zero active players remain).
     *
     * @return true if the game is over, false otherwise
     */
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

    /**
     * Counts the number of active (non-eliminated) players in the game.
     *
     * @return the number of active players
     */
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

    /**
     * Determines the winner of the game (the last remaining active player).
     *
     * @return the winning player, or null if no winner exists
     */
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

    /**
     * Notifies all observers of a game state change.
     *
     * @param message the message to send to observers
     */
    private void notifyObservers(String message) {
        setChanged();
        super.notifyObservers(message);
    }

    /**
     * Finds the index of a specific card in a player's hand.
     *
     * @param player the player whose hand to search
     * @param card the card to find
     * @return the index of the card, or -1 if not found
     */
    private int findCardIndex(Player player, Card card) {
        for (int i = 0; i < player.getCardsPlayer().size(); i++) {
            if (player.getCardsPlayer().get(i).equals(card)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Gets the current sum of cards on the table.
     *
     * @return the current sum
     */
    public int getCurrentSum() {
        return table.getCurrentSum();
    }

    /**
     * Gets the list of machine players.
     *
     * @return the list of machine players
     */
    public List<Player> getMachinePlayers() {
        return machinePlayers;
    }

    /**
     * Gets the current player whose turn it is.
     *
     * @return the current player
     */
    public Player getCurrentPlayer() {
        if (currentPlayerIndex == 0) {
            return humanPlayer;
        } else {
            return machinePlayers.get(currentPlayerIndex - 1);
        }
    }

    /**
     * Gets the total number of players in the game.
     *
     * @return the total number of players
     */
    public int getTotalPlayers() {
        return machinePlayers.size() + 1;
    }

    /**
     * Gets the human player.
     *
     * @return the human player
     */
    public Player getHumanPlayer() {
        return humanPlayer;
    }

    /**
     * Gets the game table.
     *
     * @return the table
     */
    public Table getTable() {
        return table;
    }

    /**
     * Gets the deck of cards.
     *
     * @return the deck
     */
    public Deck getDeck() {
        return deck;
    }

    /**
     * Gets the current game status message.
     *
     * @return the game status
     */
    public String getGameStatus() {
        return gameStatus;
    }

    /**
     * Gets an array of currently visible cards for the human player.
     *
     * @param posInitCardToShow the starting position of cards to show
     * @return an array of up to 4 visible cards
     */
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
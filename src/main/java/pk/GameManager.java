package pk;

import pk.fortune_cards.*;
import pk.dice.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The <code>GameManager</code> class for the Piraten Kapern game manages all
 * the games that are to be played, handing the {@link Dice}, {@link DiceFaces},
 * and {@link FortuneCard} to the correct {@link Player}, and then calculating
 * the scores using the {@link ScoreCalculator} before assigning to the
 * respective <code>Player</code>s.
 */
public class GameManager {

    private Dice dice;
    private DiceFaces[] diceFaces;
    private Player[] players;
    private ScoreCalculator scoreCalculator;
    private FortuneCardDeck deck;

    private static final Logger logger = LogManager.getFormatterLogger(GameManager.class);

    public final int NUM_GAMES;
    public final int NUM_PLAYERS;
    public static final int NUM_DICE = 8;
    public static final int MIN_PLAYERS = 2;
    public static final int MAX_PLAYERS = 5;
    public static final int MIN_GAMES = 1;
    public static final int DEFAULT_NUM_GAMES = 42;
    public static final int DISQUALIFIED_SKULL_COUNT = 3;
    public static final int ENDING_SCORE = 6000;

    /**
     * Constructor that creates a <code>GameManager</code> to manage the default
     * number of games using players with the indicated strategies.
     *
     * @param playerStrategies the strategies to assign to each <code>Player</code>
     *                         in the game.
     * @throws IllegalArgumentException if an invalid strategy or array length is
     *                                  present in <code>playerStrategies</code>
     */
    public GameManager(String[] playerStrategies) throws IllegalArgumentException {
        this(playerStrategies, DEFAULT_NUM_GAMES);
    }

    /**
     * Constructor that creates a <code>GameManager</code> to manage the given
     * custom number of games.
     *
     * @param playerStrategies the strategies to assign to each <code>Player</code>
     *                         in the game.
     * @param numGames         the number of games to play
     * @throws IllegalArgumentException if an invalid strategy or array length is
     *                                  present in <code>playerStrategies</code>, or
     *                                  the number of games is less than 1.
     */
    public GameManager(String[] playerStrategies, int numGames) throws IllegalArgumentException {
        if (playerStrategies.length < MIN_PLAYERS || playerStrategies.length > MAX_PLAYERS) {
            throw new IllegalArgumentException(
                    "ERROR: There must be between 2-5 players playing. Ensure arguments were entered correctly.");
        }
        if (numGames < MIN_GAMES) {
            throw new IllegalArgumentException(
                    "ERROR: There must be at least 1 game played. Ensure arguments were entered correctly.");
        }
        NUM_GAMES = numGames;
        NUM_PLAYERS = playerStrategies.length;
        scoreCalculator = new ScoreCalculator();
        dice = new Dice();
        diceFaces = new DiceFaces[NUM_DICE];
        deck = new FortuneCardDeck();
        players = new Player[NUM_PLAYERS];
        for (int i = 0; i < NUM_PLAYERS; i++) {
            players[i] = new Player(i, playerStrategies[i].toUpperCase().trim());
            logger.debug("Creating new player with ID %d and strategy %s", players[i].ID, players[i].strategyType);
        }
    }

    protected void resetGame() {
        logger.debug("Resetting all players for new game.");
        for (Player player : players) {
            player.resetPlayer();
        }
    }

    protected void assignScore(int playerID, FortuneCard card) {
        players[playerID].updateScore(scoreCalculator.calculateScore(card, diceFaces));
    }

    protected boolean playTurns(boolean finalTurn) {
        for (Player player : players) {
            if (!player.isDone()) {
                FortuneCard card = deck.getNextCard();
                player.playTurn(dice, card, diceFaces);
                assignScore(player.ID, card);
                player.setDone(player.getScore() >= ENDING_SCORE);
                if (!finalTurn && player.isDone()) {
                    return true;
                }
            }
        }
        return false;
    }

    protected boolean checkFinalTurn() {
        logger.debug("Checking for true final turn.");
        for (Player player : players) {
            if (player.getScore() >= ENDING_SCORE) {
                logger.debug("Player %d has a confirmed ending score.", player.ID);
                return true;
            } else {
                logger.debug("Player %d does not have an ending score.", player.ID);
                player.setDone(false);
            }
        }
        return false;
    }

    protected void updateWinner() {
        logger.debug("Updating the winner of this game.");
        int highScore = -1;
        for (Player player : players) {
            logger.debug("Player %d has a score of %d", player.ID, player.getScore());
            highScore = Math.max(highScore, player.getScore());
        }
        for (Player player : players) {
            if (player.getScore() == highScore)
                player.winGame();
        }
    }

    protected void playGame() {
        boolean finalTurn = false;
        deck.resetTopIndex();
        deck.shuffleDeck();
        while (!finalTurn) {
            finalTurn = playTurns(finalTurn);
            if (finalTurn) {
                playTurns(finalTurn);
                finalTurn = checkFinalTurn();
            }
        }
        updateWinner();
    }

    protected void playAllGames() {
        for (int i = 0; i < NUM_GAMES; i++) {
            logger.debug("############# Start game %d #############", i + 1);
            playGame();
            resetGame();
            logger.debug("############## End game %d ##############", i + 1);
        }
    }

    public double[] getPercentages() {
        logger.debug("Starting new simulation with %d players", NUM_PLAYERS);
        logger.debug("--------------- Begin Simulation ---------------");
        playAllGames();
        logger.debug("---------------- End Simulation ----------------");
        logger.debug("--------------- Calculating Wins ---------------");
        double[] percentages = new double[NUM_PLAYERS];
        for (int i = 0; i < NUM_PLAYERS; i++) {
            percentages[i] = ((0d + players[i].getWins()) / NUM_GAMES) * 100;
        }
        return percentages;
    }
}

package pk;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GameManager {

    private Dice dice;
    private Faces[] diceFaces;
    private Player[] players;
    private ScoreCalculator scoreCalculator;
    private CardDeck deck;

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

    /*
     * public GameManager() throws IllegalArgumentException, NullPointerException {
     * this(new String[]{"RANDOM", "RANDOM"}, DEFAULT_NUM_GAMES);
     * }
     */

    public GameManager(String[] playerStrategies) throws IllegalArgumentException {
        this(playerStrategies, DEFAULT_NUM_GAMES);
    }

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
        diceFaces = new Faces[NUM_DICE];
        deck = new CardDeck();
        players = new Player[NUM_PLAYERS];
        for (int i = 0; i < NUM_PLAYERS; i++) {
            players[i] = new Player(i, playerStrategies[i].toUpperCase().trim());
            logger.debug("Creating new player with ID %d and strategy %s", players[i].ID, players[i].strategyType);
        }
    }

    public void resetGame() {
        logger.debug("Resetting all players for new game.");
        for (Player player : players) {
            player.resetPlayer();
        }
    }

    public void assignScore(int playerID, Card card) {
        players[playerID].updateScore(scoreCalculator.calculateScore(card, diceFaces));
    }

    public boolean playTurns(boolean finalTurn) {
        for (Player player : players) {
            if (!player.isDone()) {
                Card card = deck.getNextCard();
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

    public boolean checkFinalTurn() {
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

    public void updateWinner() {
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

    public void playGame() {
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

    public void playAllGames() {
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

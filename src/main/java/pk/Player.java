package pk;

import java.util.*;
import pk.fortune_cards.*;
import pk.strategies.*;
import pk.dice.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Player class for Piraten Kapern game.
 */
public class Player {

    private int score;
    private int wins;
    private boolean done;

    private final Strategy strategy;

    private static final Logger logger = LogManager.getFormatterLogger(Player.class);

    public final StrategyTypes strategyType;
    public final int ID;

    /**
     * Constructor for the <code>Player</code> class which takes in an ID number and
     * the name of the intended strategy.
     *
     * @param playerID     the ID number for the player
     * @param strategyName the name of the strategy used by the player, as a
     *                     <code>String</code>
     * @throws IllegalArgumentException if the given strategy type is invalid and
     *                                  does not match any of the strategy types
     *                                  listed in {@link StrategyTypes}
     */
    public Player(int playerID, String strategyName) throws IllegalArgumentException {
        score = 0;
        wins = 0;
        ID = playerID;
        done = false;
        try {
            strategyType = StrategyTypes.valueOf(strategyName);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(String.format(
                    "ERROR: %s is not a valid Strategy type. Ensure arguments are entered correctly.", strategyName));
        } catch (NullPointerException e) {
            throw new IllegalArgumentException(
                    "ERROR: strategyName was passed as a null pointer. Ensure arguments are entered correctly.");
        }
        switch (strategyType) {
            case SMART:
                strategy = new SmartStrategy();
                break;
            case COMBO:
                strategy = new ComboStrategy();
                break;
            default:
                strategy = new RandomStrategy();
                break;
        }
    }

    /**
     * Rolls all the dice selected by the player during the current turn. During the
     * player's first roll, all dice are rolled.
     *
     * @param firstRoll flag representing whether it is the player's first roll
     *                  during their turn
     * @param dice      the {@link Dice} to roll during the game
     * @param diceFaces the faces of the 8 dice that the player may roll during
     *                  their turn
     */
    private void rollDice(boolean firstRoll, Dice dice, DiceFaces[] diceFaces) {
        // Initially roll all dice
        for (int i = 0; i < GameManager.NUM_DICE; i++) {
            if (strategy.isRolled(i) || firstRoll) {
                diceFaces[i] = dice.roll();
                logger.debug("Player %d is rolling the die at index %d", ID, i);
            }
        }
        logger.debug("Player %d rolled %s", ID, Arrays.toString(diceFaces));
    }

    /**
     * Resets the player's <code>score</code> and <code>done</code> flag for a new
     * game.
     */
    public void resetPlayer() {
        score = 0;
        done = false;
        logger.debug("Player %d is reset.", ID);
    }

    /**
     * Sets the player's <code>done</code> flag to true. Only for use with the
     * {@link GameManager} class.
     */
    protected void setDone(boolean done) {
        this.done = done;
        logger.debug("Player %d done: %b", ID, this.done);
    }

    /**
     * Gets the <code>done</code> flag value for the player.
     *
     * @return the value of the <code>done</code> boolean.
     */
    public boolean isDone() {
        return done;
    }

    /**
     * Gets the <code>score</code> value for the player.
     *
     * @return the value of the <code>score</code> int.
     */
    public int getScore() {
        return score;
    }

    /**
     * Gets the <code>wins</code> value for the player.
     *
     * @return the value of the <code>wins</code> int.
     */
    public int getWins() {
        return wins;
    }

    /**
     * Updates the player's score by the specified amount. Only for use with the
     * <code>GameManager</code> class.
     *
     * @param turnScore the integer value to update the score by
     */
    protected void updateScore(int turnScore) {
        score += turnScore;
        logger.debug("Player %d's score is %d.", ID, score);
    }

    /**
     * Plays a turn for the player during a Piraten Kapern game, using the given
     * <code>Dice</code>, {@link FortuneCard}, and {@link DiceFaces}. Only for use
     * with teh <code>GameManager</code> class.
     *
     * @param dice      the <code>Dice</code> to roll
     * @param card      the <code>FortuneCard</code> drawn by the player for the
     *                  current turn
     * @param diceFaces the faces of the 8 dice the player rolls during the game
     */
    protected void playTurn(Dice dice, FortuneCard card, DiceFaces[] diceFaces) {
        logger.debug("Player %d is playing their turn.", ID);
        logger.debug("Player's drawn card info :: %s", card);
        // Roll all dice first
        boolean isFirstRoll = true;
        rollDice(isFirstRoll, dice, diceFaces);
        // Determine the strategy based on first roll results
        strategy.strategize(isFirstRoll, card, diceFaces);
        isFirstRoll = false;
        // Until the strategy indicates that it's time to end the turn, roll whatever
        // dice the strategy has indicated and devise the new strategy from the next roll.
        while (!strategy.isEndTurn()) {
            rollDice(isFirstRoll, dice, diceFaces);
            strategy.strategize(isFirstRoll, card, diceFaces);
        }
        logger.debug("Player %d has ended their turn", ID);
    }

    /**
     * Increases the player's <code>wins</code> by 1. Only for use with the <code>GameManager</code> class.
     */
    protected void winGame() {
        wins++;
        logger.debug("Player %d wins.", ID);
    }
}

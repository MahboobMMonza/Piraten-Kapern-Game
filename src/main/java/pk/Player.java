package pk;

import java.util.*;
import pk.fortune_cards.*;
import pk.strategies.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Player {

    private int score;
    private int wins;
    private boolean done;

    private final Strategy strategy;

    private static final Logger logger = LogManager.getFormatterLogger(Player.class);

    public final StrategyTypes strategyType;
    public final int ID;

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

    private void roll(boolean firstRoll, Dice dice, Faces[] diceFaces) {
        // Initially roll all dice
        for (int i = 0; i < GameManager.NUM_DICE; i++) {
            if (strategy.isRolled(i) || firstRoll) {
                diceFaces[i] = dice.roll();
                logger.debug("Player %d is rolling the die at index %d", ID, i);
            }
        }
        logger.debug("Player %d rolled %s", ID, Arrays.toString(diceFaces));
    }

    public void resetPlayer() {
        score = 0;
        done = false;
        logger.debug("Player %d is reset.", ID);
    }

    public void setDone(boolean done) {
        this.done = done;
        logger.debug("Player %d done: %b", ID, this.done);
    }

    public boolean isDone() {
        return done;
    }

    public int getScore() {
        return score;
    }

    public int getWins() {
        return wins;
    }

    public void updateScore(int turnScore) {
        score += turnScore;
        logger.debug("Player %d's score is %d.", ID, score);
    }

    public void playTurn(Dice dice, FortuneCard card, Faces[] diceFaces) {
        logger.debug("Player %d is playing their turn.", ID);
        logger.debug("Player's drawn card info :: %s", card);
        boolean isFirstRoll = true;
        roll(isFirstRoll, dice, diceFaces);
        strategy.strategize(isFirstRoll, card, diceFaces);
        isFirstRoll = false;
        while (!strategy.isEndTurn()) {
            roll(isFirstRoll, dice, diceFaces);
            strategy.strategize(isFirstRoll, card, diceFaces);
        }
        logger.debug("Player %d has ended their turn", ID);
    }

    public void winGame() {
        wins++;
        logger.debug("Player %d wins.", ID);
    }
}

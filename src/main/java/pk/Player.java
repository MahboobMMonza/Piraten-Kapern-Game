package pk;

import java.util.*;
import pk.strategies.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Player {

    private int score;
    private int wins;
    private boolean done;
    private Strategy strategy;

    private static final Logger logger = LogManager.getFormatterLogger(Player.class);

    public final int ID;

    public Player(int playerID) {
        score = 0;
        wins = 0;
        ID = playerID;
        done = false;
        strategy = new Strategy();
    }

    public void roll(Dice dice, Faces[] diceFaces) {
        // Initially roll all dice
        for (int i = 0; i < GameManager.NUM_DICE; i++) {
            diceFaces[i] = dice.roll();
        }
        logger.debug("Player %d rolled %s", ID, Arrays.toString(diceFaces));
        strategy.strategize(diceFaces);
        while (!strategy.getMove().endTurn) {
            for (int i = 0; i < GameManager.NUM_DICE; i++) {
                if (strategy.getMove().isRolled(i)) {
                    diceFaces[i] = dice.roll();
                    logger.debug("Player %d is rolling the die at index %d", ID, i);
                }
            }
            logger.debug("Player %d rolled %s", ID, Arrays.toString(diceFaces));
            strategy.strategize(diceFaces);
        }
        logger.debug("Player %d has ended their turn", ID);
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

    public void playTurn(Dice dice, Faces[] diceFaces) {
        // Turn playing logic - for now: roll all 8 dice once, then count score
        logger.debug("Player %d is playing their turn.", ID);
        roll(dice, diceFaces);
    }

    public void winGame() {
        wins++;
        logger.debug("Player %d wins.", ID);
    }
}

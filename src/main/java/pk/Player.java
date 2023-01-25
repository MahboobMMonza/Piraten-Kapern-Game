package pk;

import java.util.*;
import pk.strategies.*;

public class Player {

    private int score;
    private int wins;
    private boolean done;
    private Strategy strategy;

    public final int ID;

    public Player(int playerID) {
        score = 0;
        wins = 0;
        ID = playerID;
        done = false;
        strategy = new Strategy(GameManager.NUM_DICE);
    }

    public void roll(Dice dice, Faces[] diceFaces) {
        StrategyMove moves;
        // Initially roll all dice
        for (int i = 0; i < GameManager.NUM_DICE; i++) {
            diceFaces[i] = dice.roll();
        }
        DebugLogger.logFormat("Player %d rolled %s", ID, Arrays.toString(diceFaces));
        moves = strategy.strategize(diceFaces);
        while (!moves.endTurn) {
            for (int i = 0; i < GameManager.NUM_DICE; i++) {
                if (moves.isRolled(i)) {
                    diceFaces[i] = dice.roll();
                    DebugLogger.logFormat("Player %d is rolling the die at index %d", ID, i);
                }
            }
            DebugLogger.logFormat("Player %d rolled %s", ID, Arrays.toString(diceFaces));
            moves = strategy.strategize(diceFaces);
        }
        DebugLogger.logFormat("Player %d has ended their turn", ID);
    }

    public void resetPlayer() {
        score = 0;
        done = false;
        DebugLogger.logFormat("Player %d is reset.", ID);
    }

    public void setDone(boolean done) {
        this.done = done;
        DebugLogger.logFormat("Player %d done: %b", ID, this.done);
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
        DebugLogger.logFormat("Player %d's score is %d.", ID, score);
    }

    public void playTurn(Dice dice, Faces[] diceFaces) {
        // Turn playing logic - for now: roll all 8 dice once, then count score
        DebugLogger.logFormat("Player %d is playing their turn.", ID);
        roll(dice, diceFaces);
    }

    public void winGame() {
        wins++;
        DebugLogger.logFormat("Player %d wins.", ID);
    }
}

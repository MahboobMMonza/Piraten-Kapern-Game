package pk;

import java.util.*;
import pk.strategies.*;

public class Player {

    public Strategy strategy;
    private int score;
    private int wins;
    private Dice dice;
    private Faces[] diceFaces;
    public static final int NUM_DICE = 8;
    public final int ID;
    private boolean done;

    public Player(int playerID) {
        score = 0;
        wins = 0;
        ID = playerID;
        dice = new Dice();
        diceFaces = new Faces[NUM_DICE];
        done = false;
        strategy = new Strategy(NUM_DICE);
    }

    public void roll() {
        // Initially roll all dice
        StrategyMove moves;
        for (int i = 0; i < NUM_DICE; i++) {
            diceFaces[i] = dice.roll();
        }
        DebugLogger.logFormat("Player %d rolled %s", ID, Arrays.toString(diceFaces));
        moves = strategy.strategize(diceFaces);
        while (!moves.endTurn) {
            for (int i = 0; i < NUM_DICE; i++) {
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

    public void reset() {
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

    public Faces[] getFaces() {
        return diceFaces;
    }

    public void updateScore(int turnScore) {
        score += turnScore;
        DebugLogger.logFormat("Player %d's score is %d.", ID, score);
    }

    public void playTurn() {
        // Turn playing logic - for now: roll all 8 dice once, then count score
        DebugLogger.logFormat("Player %d is playing their turn.", ID);
        roll();
    }

    public void winGame() {
        wins++;
        DebugLogger.logFormat("Player %d wins.", ID);
    }
}

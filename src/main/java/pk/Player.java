package pk;

import java.util.*;

public class Player {

    // public Strategy strats;
    private Random rand;
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
        rand = new Random();
        dice = new Dice();
        diceFaces = new Faces[NUM_DICE];
        done = false;
    }

    public void roll() {
        for (int i = 0; i < NUM_DICE; i++) {
            diceFaces[i] = dice.roll(rand);
        }
    }

    public void reset() {
        score = 0;
        done = false;
    }

    public void setDone(boolean done) {
        this.done = done;
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
    }

    public void playTurn() {
        // Turn playing logic - for now: roll all 8 dice once, then count score
        roll();
    }

    public void winGame() {
        wins++;
    }
}

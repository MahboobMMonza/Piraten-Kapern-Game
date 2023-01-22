package pk;

import java.util.*;

public class Player {

    // public Strategy strats;
    private Random rand;
    public int score;
    public int wins;
    public Dice dice;
    public Faces[] diceFaces;
    public static final int NUM_DICE = 8;

    public Player() {
        score = 0;
        wins = 0;
        rand = new Random();
        dice = new Dice();
        diceFaces = new Faces[NUM_DICE];
    }

    public void roll() {
        for (int i = 0; i < NUM_DICE; i++) {
            diceFaces[i] = dice.roll(rand);
        }
    }

    public void resetScore() {
        this.score = 0;
    }

    public void updateScore() {
        for (Faces face : diceFaces) {
            if (face == Faces.GOLD || face == Faces.DIAMOND) {
                this.score += 100;
            }
        }
    }

    public void playTurn() {
        // Turn playing logic - for now: roll all 8 dice once, then count score
        roll();
        updateScore();
    }

    public void winGame() {
        this.wins++;
    }
}

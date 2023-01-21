package pk;

import java.util.*;

public class Player {

    // public Strategy strats;
    public Random rand;
    public int score;
    public int wins;
    public int[] dice;

    public Player() {
        score = 0;
        wins = 0;
        rand = new Random();
        dice = new int[1];
    }

    public void roll() {
        for (int i = 0; i < dice.length; i++) {
            dice[i] = rand.nextInt(0,6);
        }
    }

    public void playTurn() {}
}

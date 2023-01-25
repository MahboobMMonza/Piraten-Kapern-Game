package pk.strategies;

import java.util.Random;
import pk.Faces;
import pk.GameManager;

public class Strategy {

    private StrategyMove move;
    private static final int MIN_NUM_DICE_ROLLED = 2;
    private final int NUM_DICE;
    private Random rand;

    public Strategy(int numDice) {
        NUM_DICE = numDice;
        move = new StrategyMove(NUM_DICE);
        rand = new Random();
    }

    public StrategyMove strategize(Faces[] diceFaces) {
        move.resetRollList();
        int skullCount = 0, numDiceRoll, diceIndex;
        for (Faces face : diceFaces) {
            if (face == Faces.SKULL) {
                skullCount++;
            }
        }
        if (skullCount >= GameManager.DISQUALIFIED_SKULL_COUNT) {
            move.endTurn = true;
            return move;
        }
        // Roll a random number of dice between 2 and max dice rolled, don't roll skulls
        numDiceRoll = rand.nextInt(NUM_DICE - skullCount - MIN_NUM_DICE_ROLLED + 1) + MIN_NUM_DICE_ROLLED;
        for (int i = 0; i < numDiceRoll; i++) {
            diceIndex = rand.nextInt(NUM_DICE);
            while (move.isRolled(diceIndex) || diceFaces[diceIndex] == Faces.SKULL) {
                diceIndex = rand.nextInt(NUM_DICE);
            }
            move.makeRoll(diceIndex);
        }
        move.endTurn = false;
        return move;
    }
}

package pk.strategies;

import java.util.Random;
import pk.DebugLogger;
import pk.Faces;
import pk.GameManager;

public class Strategy {

    private StrategyMove move;
    private static final int MIN_NUM_DICE_ROLLED = 2;
    private Random rand;

    public Strategy() {
        move = new StrategyMove();
        rand = new Random();
    }

    public StrategyMove getMove() {
        return move;
    }

    public void strategize(Faces[] diceFaces) {
        move.resetRollList();
        move.endTurn = false;
        int skullCount = 0, numDiceRoll, diceIndex;
        for (Faces face : diceFaces) {
            if (face == Faces.SKULL) {
                skullCount++;
            }
        }
        DebugLogger.logFormat("There are %d skulls currently rolled", skullCount);
        // If 3 skulls (or more), then end the turn
        if (skullCount >= GameManager.DISQUALIFIED_SKULL_COUNT) {
            move.endTurn = true;
            return;
        }
        // Roll a random number of dice between 2 and max dice rolled; don't roll skulls (skullCount <= 2)
        numDiceRoll = rand.nextInt(GameManager.NUM_DICE - skullCount - MIN_NUM_DICE_ROLLED + 1) + MIN_NUM_DICE_ROLLED;
        DebugLogger.logFormat("Rolling %d dice", numDiceRoll);
        for (int i = 0; i < numDiceRoll; i++) {
            diceIndex = rand.nextInt(GameManager.NUM_DICE);
            while (move.isRolled(diceIndex) || diceFaces[diceIndex] == Faces.SKULL) {
                diceIndex = rand.nextInt(GameManager.NUM_DICE);
            }
            // DebugLogger.logFormat("Rolling the dice at index %d", diceIndex);
            move.setRoll(diceIndex);
        }
        return;
    }
}

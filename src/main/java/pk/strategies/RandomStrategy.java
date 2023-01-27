package pk.strategies;

import java.util.Random;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pk.Faces;
import pk.GameManager;

public class RandomStrategy extends Strategy {

    private static final Logger logger = LogManager.getFormatterLogger(RandomStrategy.class);
    private static int MAX_REROLLS = 5;

    private Random rand;
    private int numRolls;

    public RandomStrategy() {
        super();
        rand = new Random();
    }

    private boolean updateNumRolls(boolean firstRoll) {
        if (firstRoll) {
            // Decide to re-roll up to MAX_REROLLS times
            numRolls = rand.nextInt(MAX_REROLLS + 1);
            logger.debug("Planning to roll %d times", numRolls);
        } else {
            numRolls--;
        }
        return (numRolls == 0);
    }

    public void strategize(boolean firstRoll, Faces[] diceFaces) {
        resetRollList();
        endTurn = updateNumRolls(firstRoll);
        if (endTurn) {
            return;
        }
        int skullCount = countSkulls(diceFaces), numDiceRoll, diceIndex;
        logger.debug("There are %d skulls currently rolled", skullCount);
        // If 3 skulls (or more), then end the turn
        if (skullCount >= GameManager.DISQUALIFIED_SKULL_COUNT) {
            endTurn = true;
            return;
        }
        // Roll a random number of dice between 2 and max dice rolled; don't roll skulls
        // (skullCount <= 2)
        numDiceRoll = rand.nextInt(GameManager.NUM_DICE - skullCount - MIN_NUM_DICE_ROLLED + 1) + MIN_NUM_DICE_ROLLED;
        logger.debug("Rolling %d dice", numDiceRoll);
        for (int i = 0; i < numDiceRoll; i++) {
            diceIndex = rand.nextInt(GameManager.NUM_DICE);
            while (isRolled(diceIndex) || diceFaces[diceIndex] == Faces.SKULL) {
                diceIndex = rand.nextInt(GameManager.NUM_DICE);
            }
            // logger.debug("Rolling the dice at index %d", diceIndex);
            setRoll(diceIndex);
        }
        return;
    }
}

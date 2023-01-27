package pk.strategies;

import java.util.Random;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pk.Faces;
import pk.GameManager;

public class RandomStrategy extends Strategy {

    private static final Logger logger = LogManager.getFormatterLogger(GameManager.class);

    private Random rand;
    private boolean endTurn;
    private boolean[] rollList;


    public RandomStrategy() {
        super();
        rand = new Random();
    }

    private void resetRollList() {
        for (int i = 0; i < rollList.length; i++) {
            rollList[i] = false;
        }
    }

    private void setRoll(int index) {
        rollList[index] = true;
    }

    public boolean isRolled(int index) {
        return rollList[index];
    }

    public boolean isEndTurn() {
        return endTurn;
    }

    public void strategize(Faces[] diceFaces) {
        resetRollList();
        endTurn = false;
        int skullCount = 0, numDiceRoll, diceIndex;
        for (Faces face : diceFaces) {
            if (face == Faces.SKULL) {
                skullCount++;
            }
        }
        logger.debug("There are %d skulls currently rolled", skullCount);
        // If 3 skulls (or more), then end the turn
        if (skullCount >= GameManager.DISQUALIFIED_SKULL_COUNT) {
            endTurn = true;
            return;
        }
        // Roll a random number of dice between 2 and max dice rolled; don't roll skulls (skullCount <= 2)
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

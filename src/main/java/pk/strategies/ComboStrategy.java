package pk.strategies;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pk.Faces;
import pk.GameManager;

/**
 * ComboStrategy
 */
public class ComboStrategy extends Strategy {

    private static final Logger logger = LogManager.getFormatterLogger(ComboStrategy.class);
    private static final int USEFUL_SET_SIZE = 4;

    private int[] faceValueCount;

    public ComboStrategy() {
        super();
        faceValueCount = new int[6];
    }

    private void countFaceValues(Faces[] diceFaces) {
        for (Faces face : diceFaces) {
            faceValueCount[face.ordinal()]++;
        }
    }

    private void resetAll() {
        resetRollList();
        for (int i = 0; i < faceValueCount.length; i++) {
            faceValueCount[i] = 0;
        }
    }

    private void setUnvaluables(Faces[] diceFaces) {
        for (int i = 0; i < diceFaces.length; i++) {
            if (diceFaces[i] != Faces.SKULL && diceFaces[i] != Faces.GOLD && diceFaces[i] != Faces.DIAMOND) {
                setRoll(i);
            }
        }
    }

    public void strategize(boolean firstRoll, Faces[] diceFaces) {
        resetAll();
        countFaceValues(diceFaces);
        endTurn = (faceValueCount[Faces.SKULL.ordinal()] >= 3);
        if (endTurn) {
            return;
        }
        /*
         * Strategy:
         *
         * Try to just go for groups of gold or diamonds, but if a group of
         * 4+ is found of the i-th face (not skulls), then emphasize that group
         * instead.
         *
         * Groups of 3 are useless unless they are gold or diamond, so re-roll
         * them as well if they aren't valuables (exceptions will apply later).
         *
         * If there is a group of 4 and the remaing dice are gold and diamonds,
         * then end the turn.
         *
         */
        int largestGroupIndex = 0, maxGroupSize = 0;
        for (int i = 0; i < 6; i++) {
            if (faceValueCount[i] > maxGroupSize && i != Faces.SKULL.ordinal()) {
                maxGroupSize = faceValueCount[i];
                largestGroupIndex = i;
            }
        }
        // Re-roll if groups are smaller than 4
        if (maxGroupSize < USEFUL_SET_SIZE) {
            if (GameManager.NUM_DICE - faceValueCount[Faces.GOLD.ordinal()] - faceValueCount[Faces.DIAMOND.ordinal()]
                    - faceValueCount[Faces.SKULL.ordinal()] > MIN_NUM_DICE_ROLLED) {
                setUnvaluables(diceFaces);
                return;
            } else {
                endTurn = true;
                return;
            }
        }
        // if (maxGroupSize >= 4) {
        // }
    }
}

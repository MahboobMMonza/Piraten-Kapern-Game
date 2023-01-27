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
    private Faces frequentFace;

    private boolean isRerollablePriority(int maxGroupSize) {
        // max group exists with 1 or 0 valuables and enough dice to reroll
        return (maxGroupSize == faceValueCount[Faces.GOLD.ordinal()] && (GameManager.NUM_DICE
                - faceValueCount[Faces.DIAMOND.ordinal()] - faceValueCount[Faces.GOLD.ordinal()]
                - faceValueCount[Faces.SKULL.ordinal()]) > MIN_NUM_DICE_ROLLED)
                || (maxGroupSize == faceValueCount[Faces.DIAMOND.ordinal()] && (GameManager.NUM_DICE
                        - faceValueCount[Faces.DIAMOND.ordinal()] - faceValueCount[Faces.GOLD.ordinal()]
                        - faceValueCount[Faces.SKULL.ordinal()]) > MIN_NUM_DICE_ROLLED)
                || (GameManager.NUM_DICE - faceValueCount[frequentFace.ordinal()]
                        - faceValueCount[Faces.DIAMOND.ordinal()] - faceValueCount[Faces.GOLD.ordinal()]
                        - faceValueCount[Faces.SKULL.ordinal()]) > MIN_NUM_DICE_ROLLED;
    }

    public ComboStrategy() {
        super();
        faceValueCount = new int[Faces.NUM_FACES];
    }

    private void getFrequentFace(Faces[] diceFaces) {
        // By default set to DIAMOND for simpler logic
        frequentFace = Faces.DIAMOND;
        int maxCount = 0;
        for (Faces face : diceFaces) {
            faceValueCount[face.ordinal()]++;
            if (faceValueCount[face.ordinal()] > maxCount && face != Faces.SKULL) {
                maxCount = faceValueCount[face.ordinal()];
                frequentFace = face;
                logger.debug("%s is the most frequent face", frequentFace);
            }
        }
    }

    private void resetAll() {
        resetRollList();
        for (int i = 0; i < faceValueCount.length; i++) {
            faceValueCount[i] = 0;
        }
    }

    private void setUnvaluables(boolean followFrequent, Faces[] diceFaces) {
        for (int i = 0; i < diceFaces.length; i++) {
            if (!followFrequent && diceFaces[i] != Faces.SKULL && diceFaces[i] != Faces.GOLD
                    && diceFaces[i] != Faces.DIAMOND) {
                setRoll(i);
            } else if (followFrequent && diceFaces[i] != Faces.SKULL && diceFaces[i] != frequentFace) {
                setRoll(i);
            }
        }
    }

    public void strategize(boolean firstRoll, Faces[] diceFaces) {
        resetAll();
        getFrequentFace(diceFaces);
        // Always play it safe and call it quits when you have 1 less than the
        // disqualified skull count
        endTurn = (faceValueCount[Faces.SKULL.ordinal()] >= GameManager.DISQUALIFIED_SKULL_COUNT - 1);
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
        int maxGroupSize = 0;
        for (int i = 0; i < Faces.NUM_FACES; i++) {
            if (faceValueCount[i] > maxGroupSize && i != Faces.SKULL.ordinal()) {
                maxGroupSize = faceValueCount[i];
            }
        }
        logger.debug("The max group size is %d of %s", maxGroupSize, frequentFace);
        if (maxGroupSize < USEFUL_SET_SIZE) {
            if (GameManager.NUM_DICE - faceValueCount[Faces.GOLD.ordinal()] - faceValueCount[Faces.DIAMOND.ordinal()]
                    - faceValueCount[Faces.SKULL.ordinal()] > MIN_NUM_DICE_ROLLED) {
                logger.debug("Rolling all non-valuables");
                setUnvaluables(false, diceFaces);
            } else {
                logger.debug("Reached a safety point with no sets of 4+");
                endTurn = true;
            }
        } else {
            if ((faceValueCount[Faces.GOLD.ordinal()] == USEFUL_SET_SIZE
                    && faceValueCount[Faces.DIAMOND.ordinal()] >= MIN_NUM_DICE_ROLLED) ||
                    (faceValueCount[Faces.DIAMOND.ordinal()] == USEFUL_SET_SIZE
                            && faceValueCount[Faces.GOLD.ordinal()] >= MIN_NUM_DICE_ROLLED)) {
                // 4-2 4-3 4-4 gold/diamond combos
                logger.debug("Reached a safety point with gold-diamond combo");
                endTurn = true;
            } else if (isRerollablePriority(maxGroupSize)) {
                // Prioritize this max group
                logger.debug("Rerolling with 4+ set priority on %s", frequentFace);
                setUnvaluables(true, diceFaces);
            } else {
                // There is a set of >= 4 but remaining are either diamond, gold, skulls, or
                // singleton
                // no point in rerolling
                logger.debug("Reached a safety point with 4+ set priority on %s", frequentFace);
                endTurn = true;
            }
        }
    }
}

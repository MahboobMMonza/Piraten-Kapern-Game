package pk.strategies;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pk.fortune_cards.*;
import pk.Faces;
import pk.GameManager;

/**
 * ComboStrategy
 */
public class ComboStrategy extends Strategy {

    private static final Logger logger = LogManager.getFormatterLogger(ComboStrategy.class);
    protected static final int USEFUL_SET_SIZE = 4;

    protected int[] faceValueCount;
    protected Faces frequentFace;

    public ComboStrategy() {
        super();
        faceValueCount = new int[Faces.NUM_FACES];
    }

    protected void determineFrequentFace(Faces[] diceFaces) {
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

    protected void resetAll() {
        resetRollList();
        for (int i = 0; i < faceValueCount.length; i++) {
            faceValueCount[i] = 0;
        }
    }

    protected boolean isRerollable(boolean followFrequent, Faces face) {
        return (face != Faces.SKULL && ((!followFrequent && face != Faces.GOLD
                && face != Faces.DIAMOND) || (followFrequent && face != frequentFace)));
    }

    protected void setUnvaluables(boolean followFrequent, Faces[] diceFaces) {
        int rollCount = 0;
        for (int i = 0; i < diceFaces.length; i++) {
            if (isRerollable(followFrequent, diceFaces[i])) {
                setRoll(i);
                rollCount++;
            }
        }
        endTurn = rollCount < MIN_NUM_DICE_ROLLED;
        logger.debug("Player ending turn: %b", endTurn);
    }

    protected boolean determineEndTurn(FortuneCard card) {
        return (faceValueCount[Faces.SKULL.ordinal()] >= GameManager.DISQUALIFIED_SKULL_COUNT
                || (card.getCardType() != FortuneCardTypes.SEA_BATTLE
                        && faceValueCount[Faces.SKULL.ordinal()] >= GameManager.DISQUALIFIED_SKULL_COUNT - 1)
                || card.getCardType() == FortuneCardTypes.SEA_BATTLE
                        && faceValueCount[Faces.SABER.ordinal()] >= card.VALUE);
    }

    protected void seaBattleStrats(FortuneCard card, Faces[] diceFaces) {
        logger.debug("Player is engaged in a sea battle and requires %d SABERs!", card.VALUE);
        frequentFace = Faces.SABER;
        if (GameManager.NUM_DICE - faceValueCount[Faces.SABER.ordinal()]
                - faceValueCount[Faces.SKULL.ordinal()] >= MIN_NUM_DICE_ROLLED) {
            logger.debug("Not enough swords in sea battle :: re-rolling non-sabers");
            setUnvaluables(true, diceFaces);
        }
    }

    protected void normalStrats(Faces[] diceFaces) {
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
         * then end the turn. Otherwise try to increase the count of the quadruple.
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
            logger.debug("No useful sets found. Re-rolling all non-valuables");
            setUnvaluables(false, diceFaces);
        } else if ((faceValueCount[Faces.GOLD.ordinal()] == USEFUL_SET_SIZE
                && faceValueCount[Faces.DIAMOND.ordinal()] >= MIN_NUM_DICE_ROLLED) ||
                (faceValueCount[Faces.DIAMOND.ordinal()] == USEFUL_SET_SIZE
                        && faceValueCount[Faces.GOLD.ordinal()] >= MIN_NUM_DICE_ROLLED)) {
            // 4-2 4-3 4-4 gold/diamond combos
            logger.debug("Reached a safety point with gold-diamond combo");
            endTurn = true;
        } else {
            // There is a useful set to work with
            logger.debug("Useful sets found. Re-rolling all non-valuables");
            setUnvaluables(true, diceFaces);
        }
    }

    protected void determineTactics(boolean firstRoll, FortuneCard card, Faces[] diceFaces) {
        switch (card.getCardType()) {
            case SEA_BATTLE:
                seaBattleStrats(card, diceFaces);
                break;
            default:
                normalStrats(diceFaces);
                break;
        }
    }

    public void strategize(boolean firstRoll, FortuneCard card, Faces[] diceFaces) {
        resetAll();
        determineFrequentFace(diceFaces);
        // Always play it safe and call it quits when you have 1 less than the
        // disqualified skull count
        endTurn = determineEndTurn(card);
        if (endTurn) {
            return;
        }
        determineTactics(firstRoll, card, diceFaces);
    }
}

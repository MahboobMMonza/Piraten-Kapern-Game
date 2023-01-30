package pk.strategies;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pk.fortune_cards.*;
import pk.dice.DiceFaces;
import pk.GameManager;

/**
 * ComboStrategy
 */
public class ComboStrategy extends Strategy {

    private static final Logger logger = LogManager.getFormatterLogger(ComboStrategy.class);
    protected static final int USEFUL_SET_SIZE = 4;

    protected int[] faceValueCount;
    protected DiceFaces frequentFace;

    public ComboStrategy() {
        super();
        faceValueCount = new int[DiceFaces.NUM_FACES];
    }

    protected void determineFrequentFace(DiceFaces[] diceFaces) {
        // By default set to DIAMOND for simpler logic
        frequentFace = DiceFaces.DIAMOND;
        int maxCount = 0;
        for (DiceFaces face : diceFaces) {
            faceValueCount[face.ordinal()]++;
            if (faceValueCount[face.ordinal()] > maxCount && face != DiceFaces.SKULL) {
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

    protected boolean isRerollable(boolean followFrequent, DiceFaces face) {
        return (face != DiceFaces.SKULL && ((!followFrequent && face != DiceFaces.GOLD
                && face != DiceFaces.DIAMOND) || (followFrequent && face != frequentFace)));
    }

    protected void setUnvaluables(FortuneCard card, boolean followFrequent, DiceFaces[] diceFaces) {
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
        return (faceValueCount[DiceFaces.SKULL.ordinal()] >= GameManager.DISQUALIFIED_SKULL_COUNT
                || (card.getCardType() != FortuneCardTypes.SEA_BATTLE
                        && faceValueCount[DiceFaces.SKULL.ordinal()] >= GameManager.DISQUALIFIED_SKULL_COUNT - 1)
                || card.getCardType() == FortuneCardTypes.SEA_BATTLE
                        && faceValueCount[DiceFaces.SABER.ordinal()] >= card.VALUE);
    }

    protected void seaBattleStrats(FortuneCard card, DiceFaces[] diceFaces) {
        logger.debug("Player is engaged in a sea battle and requires %d SABERs!", card.VALUE);
        frequentFace = DiceFaces.SABER;
        if (GameManager.NUM_DICE - faceValueCount[DiceFaces.SABER.ordinal()]
                - faceValueCount[DiceFaces.SKULL.ordinal()] >= MIN_NUM_DICE_ROLLED) {
            logger.debug("Not enough swords in sea battle :: re-rolling non-sabers");
            setUnvaluables(card, true, diceFaces);
        }
    }

    protected boolean isSafetyPoint(int maxGroupSize) {
        // Any group of 4 where there isn't at least 2 non-valuables to roll or
        // 4-2/4-3/4-4 gold diamond combos
        return (faceValueCount[DiceFaces.GOLD.ordinal()] == USEFUL_SET_SIZE
                && faceValueCount[DiceFaces.DIAMOND.ordinal()] >= MIN_NUM_DICE_ROLLED) ||
                (faceValueCount[DiceFaces.DIAMOND.ordinal()] == USEFUL_SET_SIZE
                        && faceValueCount[DiceFaces.GOLD.ordinal()] >= MIN_NUM_DICE_ROLLED)
                ||
                (maxGroupSize == faceValueCount[DiceFaces.GOLD.ordinal()] && (GameManager.NUM_DICE
                        - faceValueCount[DiceFaces.DIAMOND.ordinal()] - faceValueCount[DiceFaces.GOLD.ordinal()]
                        - faceValueCount[DiceFaces.SKULL.ordinal()]) < MIN_NUM_DICE_ROLLED)
                || (maxGroupSize == faceValueCount[DiceFaces.DIAMOND.ordinal()] && (GameManager.NUM_DICE
                        - faceValueCount[DiceFaces.DIAMOND.ordinal()] - faceValueCount[DiceFaces.GOLD.ordinal()]
                        - faceValueCount[DiceFaces.SKULL.ordinal()]) < MIN_NUM_DICE_ROLLED)
                || (GameManager.NUM_DICE - faceValueCount[frequentFace.ordinal()]
                        - faceValueCount[DiceFaces.DIAMOND.ordinal()] - faceValueCount[DiceFaces.GOLD.ordinal()]
                        - faceValueCount[DiceFaces.SKULL.ordinal()]) < MIN_NUM_DICE_ROLLED;
    }

    protected void normalStrats(FortuneCard card, DiceFaces[] diceFaces) {
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
        logger.debug("The max group size is %d of %s", faceValueCount[frequentFace.ordinal()], frequentFace);
        if (isSafetyPoint(faceValueCount[frequentFace.ordinal()])) {
            logger.debug("Reached a safety point and will end the turn.");
            endTurn = true;
        } else {
            // There is a useful set to work with
            logger.debug("Useful sets found. Re-rolling all non-valuables");
            setUnvaluables(card, faceValueCount[frequentFace.ordinal()] >= USEFUL_SET_SIZE, diceFaces);
        }
    }

    protected void determineTactics(boolean firstRoll, FortuneCard card, DiceFaces[] diceFaces) {
        switch (card.getCardType()) {
            case SEA_BATTLE:
                seaBattleStrats(card, diceFaces);
                break;
            default:
                normalStrats(card, diceFaces);
                break;
        }
    }

    public void strategize(boolean firstRoll, FortuneCard card, DiceFaces[] diceFaces) {
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

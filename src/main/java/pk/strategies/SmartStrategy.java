package pk.strategies;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pk.fortune_cards.*;
import pk.Faces;
import pk.GameManager;

/**
 * SmartStrategy
 */
public class SmartStrategy extends ComboStrategy {

    private static final Logger logger = LogManager.getFormatterLogger(SmartStrategy.class);

    @Override
    protected void setUnvaluables(FortuneCard card, boolean followFrequent, Faces[] diceFaces) {
        int saberStoreCount = 0, rollCount = 0;
        // Set frequent to SABER if not met quota
        for (int i = 0; i < diceFaces.length; i++) {
            if (card.getCardType() == FortuneCardTypes.SEA_BATTLE && diceFaces[i] == Faces.SABER
                    && saberStoreCount < card.VALUE) {
                saberStoreCount++;
            } else if (card.getCardType() == FortuneCardTypes.MONKEY_BUSINESS && followFrequent
                    && frequentFace == Faces.MONKEY && diceFaces[i] != Faces.MONKEY && diceFaces[i] != Faces.PARROT) {
                setRoll(i);
                rollCount++;
            } else if (isRerollable(followFrequent, diceFaces[i])) {
                setRoll(i);
                rollCount++;
            }
        }
        endTurn = rollCount < MIN_NUM_DICE_ROLLED;
        logger.debug("Player ending turn: %b", endTurn);
    }

    @Override
    protected boolean determineEndTurn(FortuneCard card) {
        return (card.getCardType() != FortuneCardTypes.SEA_BATTLE
                && faceValueCount[Faces.SKULL.ordinal()] >= GameManager.DISQUALIFIED_SKULL_COUNT - 1
                || faceValueCount[Faces.SKULL.ordinal()] >= GameManager.DISQUALIFIED_SKULL_COUNT);
    }

    @Override
    protected void determineTactics(boolean firstRoll, FortuneCard card, Faces[] diceFaces) {
        // Determine which strategy path to take based on the fortune card
        switch (card.getCardType()) {
            case SEA_BATTLE:
                seaBattleStrats(card, diceFaces);
                break;
            case MONKEY_BUSINESS:
                monkeyBusinessStrats(card, diceFaces);
                break;
            default:
                normalStrats(card, diceFaces);
                break;
        }
    }

    protected void monkeyBusinessStrats(FortuneCard card, Faces[] diceFaces) {
        /*
         * Strategy
         *
         * Combine the monkey and parrot count and follow the normal strategy - the
         * overriden setUnvaluables() method will account for the monkey business
         * fortune card.
         *
         */
        logger.debug("Player is assessing monkey business tactics");
        faceValueCount[Faces.MONKEY.ordinal()] += faceValueCount[Faces.PARROT.ordinal()];
        faceValueCount[Faces.PARROT.ordinal()] = 0;
        if (faceValueCount[frequentFace.ordinal()] <= faceValueCount[Faces.MONKEY.ordinal()]) {
            logger.debug("Player's most frequent face is now monkeys (and parrots) of frequency %d",
                    faceValueCount[Faces.MONKEY.ordinal()]);
            frequentFace = Faces.MONKEY;
        }
        normalStrats(card, diceFaces);
    }

    @Override
    protected void seaBattleStrats(FortuneCard card, Faces[] diceFaces) {
        /*
         * Strategy
         *
         * Re-roll all non-SABER faces as long as requirement for battle is not met. If
         * it is met,
         * the overloaded method should take care of any case automatically regardless
         * of whether
         * or not the skull is the frequent face.
         *
         */
        if (faceValueCount[Faces.SABER.ordinal()] < card.VALUE) {
            logger.debug("Not enough swords in sea battle :: re-rolling non-sabers");
            frequentFace = Faces.SABER;
            setUnvaluables(card, true, diceFaces);
        } else if (faceValueCount[Faces.SABER.ordinal()] >= card.VALUE
                && faceValueCount[Faces.SKULL.ordinal()] == GameManager.DISQUALIFIED_SKULL_COUNT - 1) {
            logger.debug("Player has obtained enough swords and will not risk skulls");
            endTurn = true;
        } else {
            logger.debug("Player has obtained enough swords and will try to maximize points");
            setUnvaluables(card, faceValueCount[frequentFace.ordinal()] >= USEFUL_SET_SIZE, diceFaces);
        }
    }
}

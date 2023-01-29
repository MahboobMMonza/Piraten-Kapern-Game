package pk.strategies;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pk.cards.*;
import pk.Faces;
import pk.GameManager;

/**
 * SmartStrategy
 */
public class SmartStrategy extends ComboStrategy {

    private static final Logger logger = LogManager.getFormatterLogger(SmartStrategy.class);

    protected void setUnvaluables(Card card, boolean followFrequent, Faces[] diceFaces) {
        int saberStoreCount = 0, rollCount = 0;
        // Set frequent to SABER if not met quota
        for (int i = 0; i < diceFaces.length; i++) {
            if (card.getCardType() == CardTypes.SEA_BATTLE && diceFaces[i] == Faces.SABER
                    && saberStoreCount < card.VALUE) {
                saberStoreCount++;
            } else if (isRerollable(followFrequent, diceFaces[i])) {
                setRoll(i);
                rollCount++;
            }
        }
        endTurn = rollCount < MIN_NUM_DICE_ROLLED;
        logger.debug("Player ending turn: %b", endTurn);
    }

    @Override
    protected boolean determineEndTurn(Card card) {
        return (card.getCardType() != CardTypes.SEA_BATTLE
                && faceValueCount[Faces.SKULL.ordinal()] >= GameManager.DISQUALIFIED_SKULL_COUNT - 1
                || faceValueCount[Faces.SKULL.ordinal()] >= GameManager.DISQUALIFIED_SKULL_COUNT);
    }

    @Override
    protected void seaBattleStrats(Card card, Faces[] diceFaces) {
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
            setUnvaluables(true, diceFaces);
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

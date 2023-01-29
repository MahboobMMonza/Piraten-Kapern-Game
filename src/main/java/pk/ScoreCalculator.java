package pk;

import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ScoreCalculator {

    private static final Logger logger = LogManager.getFormatterLogger(ScoreCalculator.class);
    private static final int DNG_POINTS = 100;
    private static final int FULL_CHEST_POINTS = 500;
    private static final int MIN_SET_SIZE = 3;
    private static final int[] SET_SCORES = { 0, 0, 0, 100, 200, 500, 1000, 2000, 4000 };
    private final int[] faceFrequencies;

    public ScoreCalculator() {
        faceFrequencies = new int[Faces.NUM_FACES];
    }

    private void countFrequencies(Faces[] diceFaces) {
        for (int i = 0; i < Faces.NUM_FACES; i++) {
            faceFrequencies[i] = 0;
        }
        for (Faces face : diceFaces) {
            faceFrequencies[face.ordinal()]++;
        }
    }

    private boolean hasFullChest() {
        int scoringDice = 0;
        for (int i = 0; i < Faces.NUM_FACES; i++) {
            if (i != Faces.SKULL.ordinal() && (faceFrequencies[i] >= MIN_SET_SIZE || i == Faces.GOLD.ordinal()
                    || i == Faces.DIAMOND.ordinal())) {
                scoringDice += faceFrequencies[i];
            }
        }
        return (scoringDice == GameManager.NUM_DICE);
    }

    public int calculateScore(Card card, Faces[] diceFaces) {
        int score = 0;
        countFrequencies(diceFaces);
        logger.debug("Player info :: %s", Arrays.toString(faceFrequencies));
        if (faceFrequencies[Faces.SKULL.ordinal()] >= GameManager.DISQUALIFIED_SKULL_COUNT
                || (card.getCardType() == CardTypes.SEA_BATTLE
                        && faceFrequencies[Faces.SABER.ordinal()] < card.VALUE)) {
            logger.debug("Player has not met scoring requirements and is disqualified.");
            if (card.getCardType() == CardTypes.SEA_BATTLE) {
                score -= card.BONUS_POINTS;
            }
            return score;
        }
        for (int i = 0; i < Faces.NUM_FACES; i++) {
            if (i != Faces.SKULL.ordinal()) {
                score += SET_SCORES[faceFrequencies[i]];
            }
        }
        score += DNG_POINTS * (faceFrequencies[Faces.GOLD.ordinal()] + faceFrequencies[Faces.DIAMOND.ordinal()]);
        if (hasFullChest()) {
            score += FULL_CHEST_POINTS;
            logger.debug("Player has a full chest!");
        }
        if (card.getCardType() == CardTypes.SEA_BATTLE) {
            logger.debug("Player has won the sea battle!");
            score += card.BONUS_POINTS;
        }
        logger.debug("Player's score changes by %d", score);
        return score;
    }
}

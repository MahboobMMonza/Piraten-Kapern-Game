package pk;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

import pk.cards.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ScoreCalculator {

    private static final Logger logger = LogManager.getFormatterLogger(ScoreCalculator.class);
    private static final int DNG_POINTS = 100;
    private static final int FULL_CHEST_POINTS = 500;
    private static final int MIN_SET_SIZE = 3;
    // private static final int[] SET_SCORES = { 0, 0, 0, 100, 200, 500, 1000, 2000,
    // 4000 };
    private static final Map<Integer, Integer> SET_SCORES;
    private final int[] faceFrequencies;

    static {
        SET_SCORES = new TreeMap<>();
        SET_SCORES.put(0, 0);
        SET_SCORES.put(1, 0);
        SET_SCORES.put(2, 0);
        SET_SCORES.put(3, 100);
        SET_SCORES.put(4, 200);
        SET_SCORES.put(5, 500);
        SET_SCORES.put(6, 1000);
        SET_SCORES.put(7, 2000);
        SET_SCORES.put(8, 4000);
    }

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

    private boolean hasFullChest(Card card) {
        int scoringDice = 0;
        for (int i = 0; i < Faces.NUM_FACES; i++) {
            if (i != Faces.SKULL.ordinal() && (faceFrequencies[i] >= MIN_SET_SIZE || i == Faces.GOLD.ordinal()
                    || i == Faces.DIAMOND.ordinal()
                    || (card.getCardType() == CardTypes.MONKEY_BUSINESS && faceFrequencies[Faces.MONKEY.ordinal()]
                            + faceFrequencies[Faces.PARROT.ordinal()] >= MIN_SET_SIZE))) {
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
        } else {
            for (int i = 0; i < Faces.NUM_FACES; i++) {
                if (i != Faces.SKULL.ordinal()) {
                    score += SET_SCORES.get(faceFrequencies[i]);
                }
            }
            if (card.getCardType() == CardTypes.MONKEY_BUSINESS) {
                score -= SET_SCORES.get(faceFrequencies[Faces.MONKEY.ordinal()]);
                score -= SET_SCORES.get(faceFrequencies[Faces.PARROT.ordinal()]);
                score += SET_SCORES
                        .get(faceFrequencies[Faces.PARROT.ordinal()] + faceFrequencies[Faces.MONKEY.ordinal()]);
            }
            if (hasFullChest(card)) {
                score += FULL_CHEST_POINTS;
                logger.debug("Player has a full chest!");
            }
            score += DNG_POINTS * (faceFrequencies[Faces.GOLD.ordinal()] + faceFrequencies[Faces.DIAMOND.ordinal()]);
            if (card.getCardType() == CardTypes.SEA_BATTLE) {
                logger.debug("Player has won the sea battle!");
                score += card.BONUS_POINTS;
            }
        }
        logger.debug("Player's score changes by %d", score);
        return score;
    }
}

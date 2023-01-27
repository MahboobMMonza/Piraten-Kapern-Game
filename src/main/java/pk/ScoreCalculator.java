package pk;

import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ScoreCalculator {

    private static final Logger logger = LogManager.getFormatterLogger(ScoreCalculator.class);
    private static final int DNG_POINTS = 100;
    private static final int[] SET_SCORES = { 0, 0, 0, 100, 200, 500, 1000, 2000, 4000 };
    private final int[] faceFrequencies;

    public ScoreCalculator() {
        faceFrequencies = new int[Faces.NUM_FACES];
    }

    private void resetFrequencies() {
        for (int i = 0; i < Faces.NUM_FACES; i++) {
            faceFrequencies[i] = 0;
        }
    }

    public int calculateScore(Faces[] diceFaces) {
        int score = 0;
        resetFrequencies();
        for (Faces face : diceFaces) {
            faceFrequencies[face.ordinal()]++;
        }
        logger.debug("Player info :: %s", Arrays.toString(faceFrequencies));
        if (faceFrequencies[Faces.SKULL.ordinal()] < GameManager.DISQUALIFIED_SKULL_COUNT) {
            for (int i = 0; i < Faces.NUM_FACES; i++) {
                if (i != Faces.SKULL.ordinal()) {
                    score += SET_SCORES[faceFrequencies[i]];
                }
            }
            score += DNG_POINTS * (faceFrequencies[Faces.GOLD.ordinal()] + faceFrequencies[Faces.DIAMOND.ordinal()]);
        } else if (faceFrequencies[Faces.SKULL.ordinal()] >= GameManager.DISQUALIFIED_SKULL_COUNT) {
            logger.debug("Player has exceeded skull count and is disqualified.");
        }
        logger.debug("Player's score changes by %d", score);
        return score;
    }
}

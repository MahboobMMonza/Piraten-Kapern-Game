package pk.fortune_cards;

import java.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * FortuneCardDeck
 */
public class FortuneCardDeck {

    private static Logger logger = LogManager.getFormatterLogger(FortuneCardDeck.class);

    private List<FortuneCard> deck;
    private int currentIndex;

    public static final int CARD_DECK_SIZE = 35;
    public static final int NUM_MONKEY_BUSINESS_CARDS = 4;
    public static final int NUM_CARDS_PER_SEA_BATTLE = 2;
    private static final int[][] SEA_BATTLE_INFO;

    static {
        SEA_BATTLE_INFO = new int[3][2];
        SEA_BATTLE_INFO[0] = new int[] { 2, 300 };
        SEA_BATTLE_INFO[1] = new int[] { 3, 500 };
        SEA_BATTLE_INFO[2] = new int[] { 4, 1000 };
    }

    public void resetTopIndex() {
        currentIndex = 0;
    }

    public FortuneCardDeck() {
        deck = new ArrayList<FortuneCard>();
        // 3 types * 2 cards per type = 6 cards
        for (int[] info : SEA_BATTLE_INFO) {
            for (int i = 0; i < NUM_CARDS_PER_SEA_BATTLE; i++) {
                deck.add(new FortuneCard(FortuneCardTypes.SEA_BATTLE, info[0], info[1]));
            }
        }
        for (int i = 0; i < NUM_MONKEY_BUSINESS_CARDS; i++) {
            deck.add(new FortuneCard(FortuneCardTypes.MONKEY_BUSINESS));
        }
        while (deck.size() < CARD_DECK_SIZE) {
            deck.add(new FortuneCard(FortuneCardTypes.NOP));
        }
        currentIndex = 0;
    }

    public void shuffleDeck() {
        logger.debug("Shuffling the card deck");
        Collections.shuffle(deck);
    }

    private void updateCurrentIndex() {
        // Redundent constant to ensure the value stays >= 0 after modulus
        currentIndex = (currentIndex + 1 + CARD_DECK_SIZE) % CARD_DECK_SIZE;
        if (currentIndex == 0) {
            shuffleDeck();
        }
    }

    public FortuneCard getNextCard() {
        logger.debug("Getting card at index %d", currentIndex);
        FortuneCard card = deck.get(currentIndex);
        updateCurrentIndex();
        return card;
    }
}

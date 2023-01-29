package pk;

import java.util.ArrayList;
import java.util.List;

/**
 * CardDeck
 */
public class CardDeck {

    private List<Card> deck;
    private int currentIndex;

    public final int CARD_DECK_SIZE; // Should always be 35

    private static final int NUM_CARDS_PER_SEA_BATTLE = 2;
    private static final int NUM_NOP_CARDS = 29;
    private static final int[][] SEA_BATTLE_INFO;

    static {
        SEA_BATTLE_INFO = new int[3][2];
        SEA_BATTLE_INFO[0] = new int[] { 2, 300 };
        SEA_BATTLE_INFO[1] = new int[] { 3, 500 };
        SEA_BATTLE_INFO[2] = new int[] { 4, 1000 };
    }

    public CardDeck() {
        deck = new ArrayList<Card>();
        // 3 types * 2 cards per type = 6 cards
        for (int[] info : SEA_BATTLE_INFO) {
            for (int i = 0; i < NUM_CARDS_PER_SEA_BATTLE; i++) {
                deck.add(new Card(CardTypes.SEA_BATTLE, info[0], info[1]));
            }
        }
        for (int i = 0; i < NUM_NOP_CARDS; i++) {
            deck.add(new Card(CardTypes.NOP));
        }
        currentIndex = 0;
        CARD_DECK_SIZE = deck.size();
    }

    private void updateCurrentIndex() {
        // Redundent constant to ensure the value stays >= 0
        currentIndex = (currentIndex + 1 + CARD_DECK_SIZE) % CARD_DECK_SIZE;
    }

    public Card getNextCard() {
        Card card = deck.get(currentIndex);
        updateCurrentIndex();
        return card;
    }
}
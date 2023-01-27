package pk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * CardDeck
 */
public class CardDeck {

    private List<Card> deck;
    private int currentIndex;

    public final int CARD_DECK_SIZE; // Should always be 35

    private static final int NUM_CARDS_PER_SEA_BATTLE = 2;
    private static final int NUM_NOP_CARDS = 29;
    private static final int NOP_CARD_VALUE = 0;
    private static final int NOP_CARD_BONUS = 0;
    private static final Map<Integer, Integer> SEA_BATTLE_INFO;

    static {
        SEA_BATTLE_INFO = new TreeMap<>();
        SEA_BATTLE_INFO.put(2, 300);
        SEA_BATTLE_INFO.put(3, 500);
        SEA_BATTLE_INFO.put(4, 1000);
    }

    public CardDeck() {
        deck = new ArrayList<Card>();
        // 3 types * 2 cards per type = 6 cards
        for (Map.Entry<Integer, Integer> entry : SEA_BATTLE_INFO.entrySet()) {
            for (int i = 0; i < NUM_CARDS_PER_SEA_BATTLE; i++) {
                deck.add(new Card(CardTypes.SEA_BATTLE, entry.getKey().intValue(), entry.getValue().intValue()));
            }
        }
        for (int i = 0; i < NUM_NOP_CARDS; i++) {
            deck.add(new Card(CardTypes.NOP, NOP_CARD_VALUE, NOP_CARD_BONUS));
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

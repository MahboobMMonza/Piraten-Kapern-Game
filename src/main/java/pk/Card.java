package pk;

/**
 * Card
 */
public class Card {

    CardTypes cardType;

    public final int VALUE, BONUS_POINTS;
    public static final int NOP_CARD_VALUE = 0;
    public static final int NOP_CARD_BONUS = 0;

    protected Card(CardTypes type, int value, int bonus) {
        cardType = type;
        switch (cardType) {
            case SEA_BATTLE:
                VALUE = value;
                BONUS_POINTS = bonus;
                break;
            default:
                VALUE = BONUS_POINTS = 0;
                break;
        }
    }

    protected Card(CardTypes type) throws IllegalArgumentException {
        cardType = type;
        switch (cardType) {
            case SEA_BATTLE:
                throw new IllegalArgumentException("ERROR: This constructor cannot be used for Sea Battle cards.");
            default:
                VALUE = NOP_CARD_VALUE;
                BONUS_POINTS = NOP_CARD_BONUS;
                break;
        }

    }
}

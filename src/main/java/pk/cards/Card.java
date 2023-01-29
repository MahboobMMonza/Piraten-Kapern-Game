package pk.cards;

/**
 * Card
 */
public class Card {

    private CardTypes cardType;

    public final int VALUE, BONUS_POINTS;
    public static final int NO_VALUE = 0;
    public static final int NO_BONUS = 0;

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
                VALUE = NO_VALUE;
                BONUS_POINTS = NO_BONUS;
                break;
        }
    }

    public CardTypes getCardType() {
        return cardType;
    }

    @Override
    public String toString() {
        return String.format("Type: %s\tValue: %d\tBonus: %03d", cardType, VALUE, BONUS_POINTS);
    }
}

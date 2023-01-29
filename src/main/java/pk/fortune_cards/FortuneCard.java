package pk.fortune_cards;

/**
 * Card
 */
public class FortuneCard {

    private FortuneCardTypes cardType;

    public final int VALUE, BONUS_POINTS;
    public static final int NO_VALUE = 0;
    public static final int NO_BONUS = 0;

    protected FortuneCard(FortuneCardTypes type, int value, int bonus) {
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

    protected FortuneCard(FortuneCardTypes type) throws IllegalArgumentException {
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

    public FortuneCardTypes getCardType() {
        return cardType;
    }

    @Override
    public String toString() {
        return String.format("Type: %s\tValue: %d\tBonus: %03d", cardType, VALUE, BONUS_POINTS);
    }
}

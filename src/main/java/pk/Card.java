package pk;

/**
 * Card
 */
public class Card {

    CardTypes cardType;

    public final int VALUE, BONUS_POINTS;

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
}

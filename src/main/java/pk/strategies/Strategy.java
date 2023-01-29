package pk.strategies;

import pk.Faces;
import pk.fortune_cards.*;
import pk.GameManager;

public abstract class Strategy {

    protected static final int MIN_NUM_DICE_ROLLED = 2;

    protected boolean endTurn;
    protected boolean[] rollList;

    public Strategy() {
        rollList = new boolean[GameManager.NUM_DICE];
    }

    protected void resetRollList() {
        for (int i = 0; i < rollList.length; i++) {
            rollList[i] = false;
        }
    }

    protected void setRoll(int index) {
        rollList[index] = true;
    }

    public boolean isRolled(int index) {
        return rollList[index];
    }

    public boolean isEndTurn() {
        return endTurn;
    }

    public abstract void strategize(boolean firstRoll, FortuneCard card, Faces[] diceFaces);
}

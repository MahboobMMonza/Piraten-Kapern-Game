package pk.strategies;

import pk.Faces;
import pk.GameManager;

public abstract class Strategy {

    protected static final int MIN_NUM_DICE_ROLLED = 2;

    private boolean endTurn;
    private boolean[] rollList;


    public Strategy() {
        rollList = new boolean[GameManager.NUM_DICE];
    }

    private void resetRollList() {
        for (int i = 0; i < rollList.length; i++) {
            rollList[i] = false;
        }
    }

    private void setRoll(int index) {
        rollList[index] = true;
    }

    public boolean isRolled(int index) {
        return rollList[index];
    }

    public boolean isEndTurn() {
        return endTurn;
    }

    public abstract void strategize(Faces[] diceFaces);

}

package pk.strategies;

import pk.GameManager;

public class StrategyMove {

    public boolean endTurn;
    private boolean[] rollList;

    protected StrategyMove() {
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

}

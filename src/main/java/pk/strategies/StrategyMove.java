package pk.strategies;

public class StrategyMove {

    public boolean endTurn;
    private boolean[] rollList;

    public StrategyMove(int numDice) {
        rollList = new boolean[numDice];
    }

    public void resetRollList() {
        for (int i = 0; i < rollList.length; i++) {
            rollList[i] = false;
        }
    }

    public void setRoll(int index) {
        rollList[index] = true;
    }

    public boolean isRolled(int index) {
        return rollList[index];
    }

}

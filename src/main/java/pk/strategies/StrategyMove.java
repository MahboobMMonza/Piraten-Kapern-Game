package pk.strategies;

public class StrategyMove {

    public boolean endTurn;
    public boolean[] rollList;

    public StrategyMove(int numDice) {
        rollList = new boolean[numDice];
    }

}

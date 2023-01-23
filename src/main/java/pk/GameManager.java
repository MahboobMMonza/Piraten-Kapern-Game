package pk;

public class GameManager {

    public Player[] players;
    public final int NUM_GAMES;
    public final int NUM_PLAYERS;
    public static final int DISQUALIFIED_SKULL_COUNT = 3;
    public static final int DNG_POINTS = 100;

    public GameManager() {
        NUM_GAMES = 42;
        NUM_PLAYERS = 2;
        players = new Player[NUM_PLAYERS];
        for (int i = 0; i < players.length; i++) {
            players[i] = new Player(i);
        }
    }

    public void resetGame() {
        for (Player player : players) {
            player.reset();
        }
    }

    public void assignScore(int playerID) {
        int goldCount = 0, diamondCount = 0, skullCount = 0;
        for (Faces face : players[playerID].getFaces()) {
            if (face == Faces.GOLD) {
                goldCount++;
            } else if (face == Faces.DIAMOND) {
                diamondCount++;
            } else if (face == Faces.SKULL) {
                skullCount++;
            }
        }
        if (skullCount < DISQUALIFIED_SKULL_COUNT) {
            players[playerID].updateScore(DNG_POINTS * (goldCount + diamondCount));
        }
    }

    public boolean playTurns(boolean finalTurn) {
        for (Player player : players) {
            if (!player.isDone()) {
                player.playTurn();
                assignScore(player.ID);
                if (!finalTurn && player.isDone()) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean checkFinalTurn() {
        for (Player player : players) {
            if (player.getScore() >= 6000) {
                return true;
            } else {
                player.resetDone();
            }
        }
        return false;
    }

    public void updateWinner() {
        int highScore = -1;
        for (Player player : players) {
            highScore = Math.max(highScore, player.getScore());
        }
        for (Player player : players) {
            if (player.getScore() == highScore)
                player.winGame();
        }
    }

    public void playGame() {
        boolean finalTurn = false;
        while (!finalTurn) {
            finalTurn = playTurns(finalTurn);
            if (finalTurn) {
                playTurns(finalTurn);
                finalTurn = checkFinalTurn();
            }
        }
    }

    public void playAllGames() {
        for (int i = 0; i < NUM_GAMES; i++) {
            playGame();
        }
    }

    public double[] getPercentages() {
        playAllGames();
        double[] percentages = new double[NUM_PLAYERS];
        for (int i = 0; i < NUM_PLAYERS; i++) {
           percentages[i] = (0d + players[i].getWins()) / NUM_GAMES;
        }
        return percentages;
    }
}

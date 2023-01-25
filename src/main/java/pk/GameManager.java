package pk;

public class GameManager {

    public Player[] players;
    public final int NUM_GAMES;
    public final int NUM_PLAYERS;
    public static final int MIN_PLAYERS = 2;
    public static final int MIN_GAMES = 1;
    public static final int DEFAULT_NUM_GAMES = 3;
    public static final int DISQUALIFIED_SKULL_COUNT = 3;
    public static final int ENDING_SCORE = 6000;
    public static final int DNG_POINTS = 100;

    public GameManager() throws IllegalArgumentException {
        this(MIN_PLAYERS, DEFAULT_NUM_GAMES);
    }

    public GameManager(int numPlayers, int numGames) throws IllegalArgumentException {
        if (numPlayers < MIN_PLAYERS) {
            throw new IllegalArgumentException("ERROR: There must be at least 2 players playing.");
        }
        if (numGames < MIN_GAMES) {
            throw new IllegalArgumentException("ERROR: There must be at least 1 game played.");
        }
        NUM_GAMES = numGames;
        NUM_PLAYERS = numPlayers;
        players = new Player[NUM_PLAYERS];
        for (int i = 0; i < players.length; i++) {
            DebugLogger.logFormat("Creating new player with ID %d", i);
            players[i] = new Player(i);
        }
    }

    public void resetGame() {
        DebugLogger.log("Resetting all players for new game.");
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
        DebugLogger.logFormat("Player %d info :: SKULLS: %d GOLD: %d DIAMOND: %d", playerID, skullCount, goldCount,
                diamondCount);
        // if (skullCount < DISQUALIFIED_SKULL_COUNT) {
        players[playerID].updateScore(DNG_POINTS * (goldCount + diamondCount));
        // }
        // if (skullCount >= DISQUALIFIED_SKULL_COUNT) {
        //     DebugLogger.logFormat("Player %d has exceeded skull count and is disqualified.", playerID);
        // }
    }

    public boolean playTurns(boolean finalTurn) {
        for (Player player : players) {
            if (!player.isDone()) {
                player.playTurn();
                assignScore(player.ID);
                player.setDone(player.getScore() >= ENDING_SCORE);
                if (!finalTurn && player.isDone()) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean checkFinalTurn() {
        DebugLogger.log("Checking for true final turn.");
        for (Player player : players) {
            if (player.getScore() >= ENDING_SCORE) {
                DebugLogger.logFormat("Player %d has a confirmed ending score.", player.ID);
                return true;
            } else {
                DebugLogger.logFormat("Player %d does not have an ending score.", player.ID);
                player.setDone(false);
            }
        }
        return false;
    }

    public void updateWinner() {
        DebugLogger.log("Updating the winner of this game.");
        int highScore = -1;
        for (Player player : players) {
            DebugLogger.logFormat("Player %d has a score of %d", player.ID, player.getScore());
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
        updateWinner();
    }

    public void playAllGames() {
        for (int i = 0; i < NUM_GAMES; i++) {
            DebugLogger.logFormat("############# Start game %d #############", i + 1);
            playGame();
            resetGame();
            DebugLogger.logFormat("############## End game %d ##############", i + 1);
        }
    }

    public double[] getPercentages() {
        DebugLogger.logFormat("Starting new simulation with %d players", NUM_PLAYERS);
        DebugLogger.log("--------------- Begin Simulation ---------------");
        playAllGames();
        DebugLogger.log("---------------- End Simulation ----------------");
        DebugLogger.log("--------------- Calculating Wins ---------------");
        double[] percentages = new double[NUM_PLAYERS];
        for (int i = 0; i < NUM_PLAYERS; i++) {
            percentages[i] = ((0d + players[i].getWins()) / NUM_GAMES) * 100;
        }
        return percentages;
    }
}

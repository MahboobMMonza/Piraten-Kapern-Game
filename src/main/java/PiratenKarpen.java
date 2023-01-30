import pk.*;

public class PiratenKarpen {

    /**
     * Main entry point for playing the Piraten Kapern game.
     *
     * @param args the arguments passed in for the game, should be the player
     *             strategies.
     */
    public static void main(String[] args) {
        try {
            GameManager manager = new GameManager(args);
            double[] percentages = manager.getPercentages();
            for (int i = 0; i < percentages.length; i++) {
                System.out.println(String.format("Player %d win percentage: %f%%", i + 1, percentages[i]));
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.out.println("GAME FAILED. TERMINATING.");
            System.exit(-1);
        }
    }

}

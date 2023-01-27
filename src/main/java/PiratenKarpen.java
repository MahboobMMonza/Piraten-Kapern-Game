import pk.*;

public class PiratenKarpen {

    public static void main(String[] args) {
        try {
            GameManager manager = new GameManager(args, 3);
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

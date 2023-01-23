import pk.*;

public class PiratenKarpen {

    public static void main(String[] args) {
        GameManager manager = new GameManager();
        double[] percentages = manager.getPercentages();
        for (int i = 0; i < percentages.length; i++) {
            System.out.printf("Player %d win percentage: %f%%", i + 1, percentages[i]);
        }
    }

}

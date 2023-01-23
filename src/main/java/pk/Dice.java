package pk;
import java.util.Random;

public class Dice {

    private static final Faces[] faceValues = Faces.values();

    public Faces roll(Random bag) {
        return faceValues[bag.nextInt(faceValues.length)];
    }

}

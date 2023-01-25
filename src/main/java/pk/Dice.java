package pk;

import java.util.Random;

public class Dice {

    private static final Faces[] faceValues = Faces.values();
    private Random bag;

    public Dice() {
        bag = new Random();
    }

    public Faces roll() {
        return faceValues[bag.nextInt(faceValues.length)];
    }

}

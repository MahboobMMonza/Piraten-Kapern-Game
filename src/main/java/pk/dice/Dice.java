package pk.dice;

import java.util.Random;

public class Dice {

    private static final DiceFaces[] faceValues = DiceFaces.values();
    private Random bag;

    public Dice() {
        bag = new Random();
    }

    public DiceFaces roll() {
        return faceValues[bag.nextInt(DiceFaces.NUM_FACES)];
    }

}

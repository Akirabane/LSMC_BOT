package Utils;

import java.awt.*;
import java.util.Random;

public class RandomColorGenerator {
    private static final Random random = new Random();

    public static Color generateRandomColor() {
        int red = random.nextInt(256);
        int green = random.nextInt(256);
        int blue = random.nextInt(256);
        return new Color(red, green, blue);
    }
}

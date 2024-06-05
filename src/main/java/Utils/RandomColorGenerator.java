package Utils;

import java.awt.*;
import java.util.Random;

public class RandomColorGenerator {
    private static final Random random = new Random();

    public static Color generateRandomColor() {
        int red = random.nextInt(256);    // Génère un nombre entre 0 et 255
        int green = random.nextInt(256);  // Génère un nombre entre 0 et 255
        int blue = random.nextInt(256);   // Génère un nombre entre 0 et 255
        return new Color(red, green, blue);
    }
}

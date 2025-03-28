package parkerbasicchessengine.Utils.randomPositions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;

public class randomPosition {

    public static String getRandomPosition() throws IOException {

        String filePath = "pchessengine\\src\\main\\java\\parkerbasicchessengine\\Utils\\randomPositions\\roughly_equal_positions.txt";

        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));

            Random random = new Random();

            return lines.get(random.nextInt(lines.size()));

        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }
}

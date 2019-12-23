package Utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class FileUtils {
    public static ArrayList<String> readAllLines(String filename) throws IOException {
        ArrayList<String> lines;
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            lines = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
                System.out.println(line);
            }
        }
        return lines;
    }
}

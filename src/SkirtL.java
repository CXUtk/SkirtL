import Utils.FileUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class SkirtL {

    public static void main(String[] args) throws IOException {
        if (args.length > 1) {
            System.err.println("Usage: SkirtL [filename]");
            System.exit(-1);
        } else if (args.length == 1) {
            runScript(args[0]);
        } else {
            runShell();
        }
    }

    /**
     * Start the command prompt and reading user input
     * @throws IOException
     */
    private static void runShell() throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        while (true){
            System.out.print("> ");
            run(reader.readLine());
        }
    }

    /**
     * Run the script file
     * @param filename
     * @throws IOException
     */
    private static void runScript(String filename) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(filename));
        run(new String(bytes, Charset.defaultCharset()));
    }

    private static void run(String src){
        SInterpreter interpreter = new SInterpreter();
        interpreter.run(src);
    }
}

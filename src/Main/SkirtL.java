package Main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SkirtL {

    public static void main(String[] args) throws IOException {
        if (args.length > 1) {
            System.err.println("Usage: Main.SkirtL [filename]");
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
        StringBuilder sb = new StringBuilder();
        SInterpreter interpreter = new SInterpreter();
        while (true){
            System.out.print("> ");
            String str = reader.readLine();
            sb.append(str);
            sb.append('\n');
            if(str.length() == 0 || (str.charAt(str.length() - 1) != ';' && str.charAt(str.length() - 1) != '}'))continue;
            interpreter.run(sb.toString(), "[Prompt]");
            // run(sb.toString(), "[Prompt]");
            sb = new StringBuilder();
        }
    }

    /**
     * Run the script file
     * @param filename
     * @throws IOException
     */
    private static void runScript(String filename) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(filename));
        run(new String(bytes, Charset.defaultCharset()), filename);
    }

    private static void run(String src, String name){
        SInterpreter interpreter = new SInterpreter();
        interpreter.run(src, name);
    }
}

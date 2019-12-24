import ErrorHandling.LexicalException;
import Lexical.Scanner;
import Lexical.TokenList;

public class SInterpreter {

    public SInterpreter(){

    }

    public void run(String str, String filename){
    	try {
		    Scanner scanner = new Scanner(str);
		    TokenList tokens = scanner.scanTokens();
		    System.out.println(tokens.toString());
	    } catch (LexicalException e){
    		System.err.println(String.format("[Lexical Error] %s (%s: line: %d)", e.getMessage(), filename, e.getLine()));
	    }
    }
}

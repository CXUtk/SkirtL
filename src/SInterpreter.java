import ErrorHandling.LexicalException;
import ErrorHandling.ParsingException;
import Lexical.Scanner;
import Lexical.Token;
import Lexical.TokenList;
import Parsing.AST.ASTPrinter;
import Parsing.Parser;

import java.util.ArrayList;

public class SInterpreter {

    public SInterpreter(){

    }

    public void run(String str, String filename){
    	try {
		    Scanner scanner = new Scanner(str);
		    ArrayList<Token> tokens = scanner.scanTokens();
		    for(Token tk : tokens){
		    	System.out.print(tk.toString() + ", ");
		    }
		    System.out.println();
		    Parser parser= new Parser(tokens);
		    ASTPrinter printer  = new ASTPrinter();
		    System.out.println(printer.getString(parser.getExpression()));
	    } catch (LexicalException e) {
		    System.err.println(String.format("[Lexical Error] %s (%s: line: %d)", e.getMessage(), filename, e.getLine()));
	    } catch (ParsingException e) {
		    System.err.println(String.format("[Parsing Error] %s (%s: line: %d)", e.getMessage(), filename, e.getLine()));
	    }
    }
}

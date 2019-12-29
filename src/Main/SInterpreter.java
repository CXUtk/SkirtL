package Main;

import ErrorHandling.LexicalException;
import ErrorHandling.ParsingException;
import Lexical.Scanner;
import Lexical.Token;
import Lexical.TokenList;
import Parsing.AST.ASTPrinter;
import Parsing.Parser;

import java.util.ArrayList;

public class SInterpreter {

	private static boolean hadError;

	public SInterpreter(){
		hadError = false;
    }

	public static void error(int line, int col, String where, String message){
		report(line, col, where, message);
	}

	private static void report(int line, int col, String where, String message) {
		System.err.println(
				"[line " + line + "] Error" + ": " + message);
		System.err.println(where);
		for(int i = 0; i < col; i++)
			System.err.print(" ");
		System.err.println("^");
		hadError = true;
	}

    public void run(String str, String filename){
    	try {
		    Scanner scanner = new Scanner(str);
		    ArrayList<Token> tokens = scanner.scanTokens();
		    if(hadError) return;
		    for(Token tk : tokens){
		    	System.out.print(tk.toString() + ", ");
		    }
		    System.out.println();
		    Parser parser = new Parser(tokens);
		    ASTPrinter printer  = new ASTPrinter();
		    System.out.println(printer.getString(parser.getExpression()));
	    } catch (LexicalException e) {
		    System.err.println(String.format("[Lexical Error] %s (%s: line: %d)", e.getMessage(), filename, e.getLine()));
	    } catch (ParsingException e) {
		    // System.err.println(String.format("[Parsing Error] %s (%s: line: %d)", e.getMessage(), filename, e.getLine()));
	    }
    }
}

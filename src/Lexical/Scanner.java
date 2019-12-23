package Lexical;

import ErrorHandling.LexicalException;

import java.util.ArrayList;

public class Scanner {
    private int start;
    private int current;
    private int currentLine;
    private String source;
    private ArrayList<Token> tokenList;
	public Scanner(String src){
        start = current = 0;
        currentLine = 1;
        source = src;
	}

	public TokenList scanTokens() throws LexicalException {
        tokenList = new ArrayList<>();
		while(!isEOF()){
		    start = current;
		    scanNext();
        }
		tokenList.add(new Token("", TokenType.EOF, null, currentLine));
		return new TokenList(tokenList);
	}

    private void scanNext() throws LexicalException {
        char c = advance();
        switch (c) {
            case ' ':
            case '\r':
            case '\t':
                // Ignore whitespace.
                break;
            case '\n':
                currentLine++;
                break;
            case '(':
                addToken(TokenType.LEFT_PARENTHESES);
                break;
            case ')':
                addToken(TokenType.RIGHT_PARENTHESES);
                break;
            case '{':
                addToken(TokenType.LEFT_BRACE);
                break;
            case '}':
                addToken(TokenType.RIGHT_BRACE);
                break;
            case ',':
                addToken(TokenType.COMMA);
                break;
            case '.':
                addToken(TokenType.DOT);
                break;
            case '-':
                addToken(TokenType.MINUS);
                break;
            case '+':
                addToken(TokenType.PLUS);
                break;
            case ';':
                addToken(TokenType.SEMICOLON);
                break;
            case '*':
                addToken(TokenType.STAR);
                break;
            case '^':
                addToken(TokenType.POWER);
                break;
            case '>':
                addToken(match('=') ? TokenType.GREATER_THAN_EQUAL_TO : TokenType.GREATER_THAN);
                break;
            case '<':
                addToken(match('=') ? TokenType.LESS_THAN_EQUAL_TO : TokenType.LESS_THAN);
                break;
            case '=':
                addToken(match('=') ? TokenType.EQUAL_TO : TokenType.ASSIGNMENT);
                break;
            case '!':
                addToken(match('=') ? TokenType.NOT_EQUAL_TO : TokenType.NOT);
                break;
            case '/':
                scanSlash();
                break;
            case '\"': {
                scanString();
                break;
            }
            default: {
                throw new LexicalException(currentLine, 0, String.format("Unexpected Token: '%c'", c));
            }
        }
    }

    private void scanString() throws LexicalException {
        while (!isEOF() && source.charAt(current) != '\"') {
            if(source.charAt(current) == '\n') currentLine++;
            advance();
        }
        if(isEOF()) throw new LexicalException(currentLine, 0, "Unterminated string");
        advance();
        String value = source.substring(start + 1, current - 1);
        addToken(TokenType.STRING, value);
    }

    private void scanSlash() {
        if(match('/')){
            while(!isEOF() && source.charAt(current) != '\n')
                advance();
        }
        else{
            addToken(TokenType.SLASH);
        }
    }

    private boolean match(char c) {
        if (isEOF()) return false;
        if (source.charAt(current) != c) return false;
        ++current;
        return true;
    }

    private char advance(){
	    ++current;
	    return source.charAt(current - 1);
    }

    private void addToken(TokenType type){
        addToken(type, null);
    }

    private void addToken(TokenType type, Object val) {
        String text = source.substring(start, current);
        tokenList.add(new Token(text, type, val, currentLine));
    }

    private boolean isEOF(){
	    return current >= source.length();
    }
}

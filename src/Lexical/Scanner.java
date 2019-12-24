package Lexical;

import ErrorHandling.LexicalException;

import java.util.ArrayList;
import java.util.Hashtable;


public class Scanner {
    private int start;
    private int current;
    private int currentLine;
    private String source;
    private ArrayList<Token> tokenList;

    private static final Hashtable<String, TokenType> keywords;
    static {
        keywords = new Hashtable<>();
        keywords.put("and", TokenType.AND);
        keywords.put("or", TokenType.OR);
        keywords.put("class", TokenType.CLASS);
        keywords.put("if", TokenType.IF);
        keywords.put("else", TokenType.ELSE);
        keywords.put("false", TokenType.FALSE);
        keywords.put("for", TokenType.FOR);
        keywords.put("function", TokenType.FUNCTION);
        keywords.put("null", TokenType.NULL);
        // keywords.put("print", );
        keywords.put("return", TokenType.RETURN);
        keywords.put("super", TokenType.SUPER);
        keywords.put("this", TokenType.THIS);
        keywords.put("true", TokenType.TRUE);
        keywords.put("var", TokenType.VAR);
        keywords.put("while", TokenType.WHILE);
    }


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
                if(isAlphaNumeric(c)){
                    scanNumber();
                }
                else if (isDigit(c)) {
                    scanIdentifier();
                }
                else {
                    throw new LexicalException(currentLine, 0, String.format("Unexpected Token: '%c'", c));
                }
            }
        }
    }

    private void scanIdentifier() {
	    while(isAlphaNumeric(getCurrent())) advance();
        String text = source.substring(start, current);
        if(keywords.contains(text)){
            addToken(keywords.get(text));
        }
        else{
            addToken(TokenType.IDENTIFIER);
        }
    }

    private void scanNumber() throws LexicalException {
	    while(isDigit(getCurrent())) advance();
	    boolean fraction = false;
	    if(getCurrent() == '.' && isDigit(getNext())){
	        advance();
            fraction = true;
	        while(isDigit(getCurrent())) advance();
        }
	    try {
            if (fraction) {
                addToken(TokenType.DIGIT, Double.parseDouble(source.substring(start, current)));
            } else {
                addToken(TokenType.DIGIT, Integer.parseInt(source.substring(start, current)));
            }
        } catch (NumberFormatException e){
	        throw new LexicalException(currentLine, 0, "Number is invalid");
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

    private char getNext() {
        if (current + 1 >= source.length()) return '\0';
        return source.charAt(current + 1);
    }

    private char getCurrent() {
        if (current >= source.length()) return '\0';
        return source.charAt(current);
    }

    private char advance(){
        if (current >= source.length()) return '\0';
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

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') ||
                (c >= 'A' && c <= 'Z') ||
                c == '_';
    }

    private boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isDigit(c);
    }
}

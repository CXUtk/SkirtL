package Lexical;

/**
 Token class contains the description of the token, linenumber, type, etc.
 */
public class Token {
    private String text;
    private TokenType type;
    private Object value;
    private int lineNumber;
    private int colNumber;

    public TokenType getType() {
        return type;
    }
    public Object getValue() {
        return value;
    }
    public int getColNumber() {
        return colNumber;
    }
    public String getText() {
        return text;
    }
    public int getLineNumber() {
        return lineNumber;
    }

    public Token(String text, TokenType type, Object val, int lineNumber, int colNumber) {
        this.text = text;
        this.type = type;
        this.lineNumber = lineNumber;
        this.value = val;
        this.colNumber = colNumber;
    }

    @Override
    public String toString() {
        return String.format("\"%s\": %s", text, type.name());
    }
}

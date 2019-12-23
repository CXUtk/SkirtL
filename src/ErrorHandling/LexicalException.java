package ErrorHandling;


/**
 This class is used for format exception output string
 */
public class LexicalException extends Exception {
    private int line, col;
    public LexicalException(int line, int col, String errormessage) {
        super(errormessage);
        this.line = line;
        this.col = col;
    }
    public int getCol() {
        return col;
    }
    public int getLine() {
	    return line;
    }
}

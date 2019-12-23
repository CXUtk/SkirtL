package ErrorHandling;


/**
 This class is used for format exception output string
 */
public class ParsingException extends Exception {
    private int line, col;
    public ParsingException(int line, int col, String errormessage) {
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

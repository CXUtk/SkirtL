package ErrorHandling;


/**
 This class is used for format exception output string
 */
public class ParsingException extends RuntimeException {
    private int line, col;
    public ParsingException(String errormessage) {
        super(errormessage);
    }
    public int getCol() {
        return col;
    }
    public int getLine() {
	    return line;
    }
}

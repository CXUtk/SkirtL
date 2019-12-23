package Lexical;

import ErrorHandling.ParsingException;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * This class is used to deal with EOF or underflow exception
 * prevent ArrayList<Token> to throw unexpected exceptions
 */
public class TokenList implements Iterable<Token> {
    private int curIndex;
    private ArrayList<Token> tokens;

    public TokenList(ArrayList<Token> tokens) {
        this.tokens = tokens;
    }

    /**
     * Get the next token in the token stream, and move the pointer forward by 1
     *
     * @return
     * @throws ParsingException
     */
    public Token getNext() throws ParsingException {
        ++curIndex;
        return tokens.get(curIndex);
    }

    /**
     * Get the token from current pointer
     *
     * @return
     * @throws ParsingException
     */
    public Token getCurrent() throws ParsingException {
        return tokens.get(curIndex);
    }

    /**
     * Get the token before the pointer, and move the pointer backward by 1
     *
     * @return
     * @throws ParsingException
     */
    public Token getPrev() throws ParsingException {
        --curIndex;
        return tokens.get(curIndex);
    }
    /**
     * Expect next token to be the token you want, throw error if it it not
     * @param type
     * @param expectInfo
     * @throws ParsingException
     */
    public void expect(TokenType type, String expectInfo) throws ParsingException {
        Token tk = getNext();
    }

    /**
     * Move the pointer the to the given position
     * @param index
     * @throws ParsingException
     */
    public void backtrack(int index) throws ParsingException {
        curIndex = index;
    }

    /**
     * Get the position of the pointer
     * @return
     */
    public int getCurrentPosition() {
        return curIndex;
    }

    public int size(){return tokens.size();}

    public boolean atEOF() {
        return tokens.get(curIndex).getType() == TokenType.EOF;
    }

    @Override
    public Iterator<Token> iterator() {
        return tokens.iterator();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(Token token : tokens){
            sb.append(token.toString());
            sb.append('\n');
        }
        return sb.toString();
    }
}
package Parsing;

import ErrorHandling.ParsingException;
import Lexical.Token;
import Lexical.TokenList;
import Lexical.TokenType;
import Parsing.AST.Expr;

import javax.swing.text.html.parser.AttributeList;
import java.util.ArrayList;

/**
 * expression     → equality ;
 * equality       → comparison ( ( "!=" | "==" ) comparison )* ;
 * comparison     → addition ( ( ">" | ">=" | "<" | "<=" ) addition )* ;
 * addition       → multiplication ( ( "-" | "+" ) multiplication )* ;
 * multiplication → unary ( ( "/" | "*" ) unary )* ;
 * unary          → ( "!" | "-" )? power
 * power          -> primary ( "^" unary)*
 * primary        → NUMBER | STRING | "false" | "true" | "nil"
 *                | "(" expression ")" ;
 */
public class Parser {
    private ArrayList<Token> tokenList;
    private int current;

    public Parser(ArrayList<Token> tokens){
        this.tokenList = tokens;
        this.current = 0;
    }

    public Expr getExpression() throws ParsingException {
        return expression();
    }

    /**
     * [expression]  ->  [equality]
     * @return
     */
    private Expr expression() throws ParsingException {
        return equality();
    }

    /**
     * [equality]  ->  [comparison] ([op_equal] [comparison])*
     * @return
     */
    private Expr equality() throws ParsingException {
        Expr expr = comparison();
        while(match(TokenType.NOT_EQUAL_TO, TokenType.EQUAL_TO)){
            Token op = getPrev();
            expr = new Expr.Binary(expr, op, comparison());
        }
        return expr;
    }

    /**
     * [comparison]     → [addition] ( ( ">" | ">=" | "<" | "<=" ) [addition] )*
     * @return
     */
    private Expr comparison() throws ParsingException {
        Expr expr = addition();
        while(match(TokenType.LESS_THAN, TokenType.LESS_THAN_EQUAL_TO,
                TokenType.GREATER_THAN, TokenType.GREATER_THAN_EQUAL_TO)){
            Token op = getPrev();
            expr = new Expr.Binary(expr, op, addition());
        }
        return expr;
    }

    /**
     * [addition]       → [multiplication] ( ( "-" | "+" ) [multiplication] )*
     * @return
     */
    private Expr addition() throws ParsingException {
        Expr expr = multiplication();
        while(match(TokenType.MINUS, TokenType.PLUS)){
            Token op = getPrev();
            expr = new Expr.Binary(expr, op, multiplication());
        }
        return expr;
    }

    /**
     * [multiplication] → [unary] ( ( "/" | "*" ) [unary] )*
     * @return
     */
    private Expr multiplication() throws ParsingException {
        Expr expr = unary();
        while(match(TokenType.SLASH, TokenType.STAR)){
            Token op = getPrev();
            expr = new Expr.Binary(expr, op, unary());
        }
        return expr;
    }

    /**
     * [unary]          → ( "!" | "-" )? [power]
     * @return
     */
    private Expr unary() throws ParsingException {
        if(match(TokenType.NOT, TokenType.MINUS, TokenType.PLUS)){
            Token op = getPrev();
            return new Expr.Unary(op, power());
        }
        return power();
    }

    private Expr power() throws ParsingException{
        Expr expr = primary();
        while(match(TokenType.POWER)){
            Token op = getPrev();
            expr = new Expr.Binary(expr, op, unary());
        }
        return expr;
    }

    /**
     * [primary] → NUMBER | STRING | "false" | "true" | "null"
     *         | "(" [expression] ")" ;
     * @return
     */
    private Expr primary() throws ParsingException {
        if(match(TokenType.FALSE))return new Expr.Literal(false);
        if(match(TokenType.TRUE))return new Expr.Literal(true);
        if(match(TokenType.NULL))return new Expr.Literal(null);
        if(match(TokenType.STRING)) return new Expr.Literal(getPrev().getValue());
        if(match(TokenType.DIGIT)) return new Expr.Literal(getPrev().getValue());
        if(match(TokenType.LEFT_PARENTHESES)){
            Expr expr = expression();
            expect(TokenType.RIGHT_PARENTHESES);
            return new Expr.Grouping(expr);
        }
        throw new ParsingException(getCurrent().getLineNumber(), 0, "Invalid expression");
    }

    private void expect(TokenType type) throws ParsingException {
        if(getCurrent().getType() != type){
            throw new ParsingException(getCurrent().getLineNumber(), 0, "Expected ')'");
        }
        advance();
    }

    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }
        return false;
    }

    private Token advance() {
        ++current;
        return tokenList.get(current - 1);
    }

    private boolean check(TokenType type) {
        if (isAtEnd()) return false;
        return getCurrent().getType() == type;
    }

    private boolean isAtEnd() {
        return tokenList.get(current).getType() == TokenType.EOF;
    }

    private Token getCurrent() {
        return tokenList.get(current);
    }
    private Token getPrev(){
        return tokenList.get(current - 1);
    }

    public static void main(String[] args){

    }

}

package Parsing;

import ErrorHandling.ParsingException;
import Lexical.Token;
import Lexical.TokenList;
import Lexical.TokenType;
import Main.SInterpreter;
import Parsing.AST.Expr;
import Parsing.AST.Stmt;

import javax.swing.text.html.parser.AttributeList;
import java.util.ArrayList;

/**
 * expression     → assignment
 * equality       → comparison ( ( "!=" | "==" ) comparison )*
 * comparison     → addition ( ( ">" | ">=" | "<" | "<=" ) addition )*
 * addition       → multiplication ( ( "-" | "+" ) multiplication )*
 * multiplication → unary ( ( "/" | "*" ) unary )*
 * unary          → ( "!" | "-" )? power
 * power          → primary ( "^" unary)*
 * primary        → NUMBER | STRING | "false" | "true" | "nil"
 *                | "(" expression ")" | IDENTIFIER
 * assignment     → IDENTIFIER "=" assignment
 *            | equality ;
 *
 * program        → statement* EOF
 * declaration    → varDecl | statement
 * varDecl        → "var" IDENTIFIER "=" expression
 * statement      → exprStmt | printStmt
 *  exprStmt       → expression ";"
 *  printStmt      → "print" expression ";"
 *
 */
public class Parser {
    private ArrayList<Token> tokenList;
    private int current;

    public Parser(ArrayList<Token> tokens){
        this.tokenList = tokens;
        this.current = 0;
    }


    public ArrayList<Stmt> parse() {
        ArrayList<Stmt> stmtList = new ArrayList<>();
        while(!isAtEnd()){
            stmtList.add(declaration());
        }
        return stmtList;
    }

    private Stmt declaration() {
        if (match(TokenType.VAR)) return varDeclaration();
        return statement();
    }

    private void synchronize() {
    }

    private Stmt varDeclaration() {
        Token name = expect(TokenType.IDENTIFIER, "Expect variable name");
        expect(TokenType.ASSIGNMENT, "Expect '=' for variable initialization");
        Expr expr = expression();
        expect(TokenType.SEMICOLON, "Expect ';' at the end of statement");
        return new Stmt.Var(name, expr);
    }

    private Stmt statement() {
        if(match(TokenType.PRINT)) return printStatement();
        return expressionStatement();
    }

    private Stmt expressionStatement() {
        Expr expr = expression();
        expect(TokenType.SEMICOLON, "Expect ';' at the end of statement");
        return new Stmt.Expression(expr);
    }

    private Stmt printStatement() {
        Expr expr = expression();
        expect(TokenType.SEMICOLON, "Expect ';' at the end of statement");
        return new Stmt.Print(expr);
    }

    /**
     * [expression]  ->  [equality]
     * @return
     */
    private Expr expression() throws ParsingException {
        return assignment();
    }

    private Expr assignment(){
        Expr expr = equality();
        if(match(TokenType.ASSIGNMENT)){
            Token op = getPrev();
            Expr assign = assignment();
            if(expr instanceof Expr.Variable){
                Token name = ((Expr.Variable)expr).getName();
                return new Expr.Assign(name, assign);
            }
            throw error(op, "Expect left value for assignment target");
        }
        return expr;
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
            expect(TokenType.RIGHT_PARENTHESES, "expect ')' for expression");
            return new Expr.Grouping(expr);
        }
        else if(match(TokenType.IDENTIFIER)){
            return new Expr.Variable(getPrev());
        }
        throw error(getCurrent(), "Invalid expression");
    }

    private Token expect(TokenType type, String text) throws ParsingException {
        if(getCurrent().getType() != type){
            throw error(getCurrent(), text);
        }
        return advance();
    }


    private ParsingException error(Token token, String text){
        SInterpreter.error(token.getLineNumber(), 0, token.getText(), text);
        return new ParsingException(text);
    }

    /**
     * Match current token with these types, if matched return true and advance
     * @param types
     * @return
     */
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

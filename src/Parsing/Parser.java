package Parsing;

import ErrorHandling.ParsingException;
import Lexical.Token;
import Lexical.TokenList;
import Lexical.TokenType;
import Main.SInterpreter;
import Parsing.AST.Expr;
import Parsing.AST.Stmt;

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
 *            | logic_or ;
 * logic_or       → logic_and ("or" logic_and)*
 * logic_and       → equality ("and" equality)*
 *
 * program        → declaration* EOF
 * declaration    → varDecl | statement
 * varDecl        → "var" IDENTIFIER "=" expression ";"
 * statement      → exprStmt | printStmt | block | ifStmt | whileStmt |forStmt |
 *  exprStmt       → expression ";"
 *  printStmt      → "print" expression ";"
 *  block          → "{" declaration* "}"
 *  ifStmt         → "if" "(" expression ")" block ("else" block)?
 *  whileStmt      → "while" "(" expression ")" block
 *  forStmt        → "for" "(" declaration?; expression?; declaration?")"  block
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

    private Stmt varDeclaration() {
        Token name = expect(TokenType.IDENTIFIER, "Expect variable name");
        expect(TokenType.ASSIGNMENT, "Expect '=' for variable initialization");
        Expr expr = expression();
        expect(TokenType.SEMICOLON, "Expect ';' at the end of statement");
        return new Stmt.Var(name, expr);
    }

    private Stmt statement() {
        if(match(TokenType.PRINT)) return printStatement();
        else if(match(TokenType.LEFT_BRACE)) return new Stmt.Block(block());
        else if(match(TokenType.IF)) return ifStatement();
        else if(match(TokenType.WHILE)) return whileStatement();
        else if(match(TokenType.FOR)) return forStmt();
        return expressionStatement();
    }

    /**
     * forStmt        → "for" "(" declaration?; expression?; declaration?")"  block
     * @return
     */
    private Stmt forStmt() {
        expect(TokenType.LEFT_PARENTHESES, "Expect '(' on for statement condition");
        Stmt init = null;
        Expr cond = null, upd = null;
        if (!match(TokenType.SEMICOLON))
            init = declaration();
        if (!check(TokenType.SEMICOLON))
            cond = expression();
        expect(TokenType.SEMICOLON, "Expect ';' on for statement");
        if (!check(TokenType.RIGHT_PARENTHESES))
            upd = expression(); // no declaration
        expect(TokenType.RIGHT_PARENTHESES, "Expect ')' on for statement");
        expect(TokenType.LEFT_BRACE, "Expect '{' on for statement");
        Stmt.Block block = new Stmt.Block(block());
        return new Stmt.For(init, cond, upd, block);
    }

    /**
    *  whileStmt      → "while" "(" expression ")" block
    */
    private Stmt whileStatement() {
        expect(TokenType.LEFT_PARENTHESES, "Expect '(' on while statement condition");
        Expr cond = expression();
        expect(TokenType.RIGHT_PARENTHESES, "Expect ')' on while statement condition");
        expect(TokenType.LEFT_BRACE, "Expect '{' on while statement");
        Stmt.Block block = new Stmt.Block(block());
        return new Stmt.While(cond, block);
    }

    private Stmt ifStatement() {
        expect(TokenType.LEFT_PARENTHESES, "Expect '(' on if statement condition");
        Expr cond = expression();
        expect(TokenType.RIGHT_PARENTHESES, "Expect ')' on if statement condition");
        expect(TokenType.LEFT_BRACE, "Expect '{' on if statement");
        Stmt.Block block = new Stmt.Block(block());
        if(match(TokenType.ELSE)){
            expect(TokenType.LEFT_BRACE, "Expect '{' on if statement");
            Stmt.Block elseBlock = new Stmt.Block(block());
            return new Stmt.If(cond, block, elseBlock);
        }
        return new Stmt.If(cond, block, null);
    }

    private ArrayList<Stmt> block() {
        ArrayList<Stmt> statements = new ArrayList<>();
        while(!check(TokenType.RIGHT_BRACE) && !isAtEnd()){
            statements.add(declaration());
        }
        expect(TokenType.RIGHT_BRACE, "Expect '}' at the end of the block");
        return statements;
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
        Expr expr = logic_or();
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

    private Expr logic_or() {
        Expr expr = logic_and();
        while(match(TokenType.OR)){
            Token op = getPrev();
            expr = new Expr.Logical(expr, op, logic_and());
        }
        return expr;
    }

    private Expr logic_and() {
        Expr expr = equality();
        while(match(TokenType.AND)){
            Token op = getPrev();
            expr = new Expr.Logical(expr, op, equality());
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

package Parsing.AST;

import Lexical.Token;
import Lexical.TokenType;

import java.util.ArrayList;

public class ASTPrinter implements Expr.Visitor<String>, Stmt.Visitor<String> {

    public String getString(Expr expr) {
        return expr.accept(this);
    }

    @Override
    public String visitBinaryExpr(Expr.Binary expr) {
        return parenthesize(expr.getOperator().getText(), expr.getLeft(), expr.getRight());
    }

    @Override
    public String visitGroupingExpr(Expr.Grouping expr) {
        return expr.getExpression().accept(this);
    }

    @Override
    public String visitLiteralExpr(Expr.Literal expr) {
        // If value is null means it is `null`
        if(expr.getValue() == null) return "null";
        if(expr.getValue() instanceof String) return String.format("\"%s\"", expr.getValue());
        return expr.getValue().toString();
    }

    @Override
    public String visitUnaryExpr(Expr.Unary expr) {
        String str = expr.getOperator().getText();
        if(expr.getOperator().getType() == TokenType.MINUS) str = "neg";
        else if(expr.getOperator().getType() == TokenType.NOT) str = "not";
        return parenthesize(str, expr.getRight());
    }

    @Override
    public String visitVariableExpr(Expr.Variable expr) {
        return expr.getName().getText();
    }

    @Override
    public String visitAssignExpr(Expr.Assign expr) {
        return parenthesize(String.format("assign [%s]", expr.getName().getText()), expr.getValue());
    }

    private String parenthesize(String name, Expr... exprs) {
        StringBuilder builder = new StringBuilder();

        builder.append("(").append(name);
        for (Expr expr : exprs) {
            builder.append(" ");
            builder.append(expr.accept(this));
        }
        builder.append(")");

        return builder.toString();
    }

    private String blockize(Stmt.Block block) {
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        for (Stmt stmt: block.getStatements()) {
            builder.append(stmt.accept(this));
            builder.append(",");
        }
        builder.append("}");
        return builder.toString();
    }

    public static void main(String[] args) {
        Expr expression = new Expr.Binary(
                new Expr.Unary(
                        new Token("-", TokenType.MINUS, null, 1, 0),
                        new Expr.Literal(123)),
                new Token("*", TokenType.STAR, null, 1, 0),
                new Expr.Grouping(
                        new Expr.Literal(45.67)));

        System.out.println(new ASTPrinter().getString(expression));
    }

    @Override
    public String visitExpressionStmt(Stmt.Expression stmt) {
        return stmt.getExpression().accept(this);
    }

    @Override
    public String visitPrintStmt(Stmt.Print stmt) {
        return parenthesize("print", stmt.getExpression());
    }

    @Override
    public String visitVarStmt(Stmt.Var stmt) {
        return parenthesize(String.format("var [%s]", stmt.getName().getText()), stmt.getInitializer());
    }

    @Override
    public String visitBlockStmt(Stmt.Block stmt) {
        return blockize(stmt);
    }

    @Override
    public String visitIfStmt(Stmt.If stmt) {
        StringBuilder builder = new StringBuilder();
        builder.append("(If ");
        builder.append(parenthesize("", stmt.getCondition()));
        builder.append(blockize(stmt.getThenBlock()));
        if (stmt.getElseBlock() != null)
            builder.append(blockize(stmt.getElseBlock()));
        builder.append(")");
        return builder.toString();
    }
}

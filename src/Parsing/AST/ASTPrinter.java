package Parsing.AST;

import Lexical.Token;
import Lexical.TokenType;

public class ASTPrinter implements Expr.Visitor<String> {

    public String getString(Expr expr) {
        return expr.accept(this);
    }

    @Override
    public String visitBinaryExpr(Expr.Binary expr) {
        return parenthesize(expr.operator.getText(), expr.left, expr.right);
    }

    @Override
    public String visitGroupingExpr(Expr.Grouping expr) {
        return expr.expression.accept(this);
    }

    @Override
    public String visitLiteralExpr(Expr.Literal expr) {
        // If value is null means it is `null`
        if(expr.value == null) return "null";
        return expr.value.toString();
    }

    @Override
    public String visitUnaryExpr(Expr.Unary expr) {
        String str = expr.operator.getText();
        if(expr.operator.getType() == TokenType.MINUS) str = "neg";
        else if(expr.operator.getType() == TokenType.NOT) str = "not";
        return parenthesize(str, expr.right);
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
}

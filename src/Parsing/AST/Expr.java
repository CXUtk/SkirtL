package Parsing.AST;

import Lexical.Token;

import java.util.ArrayList;

public abstract class Expr {
    public interface Visitor<R> {
        R visitBinaryExpr(Binary expr);

        R visitGroupingExpr(Grouping expr);

        R visitLiteralExpr(Literal expr);

        R visitUnaryExpr(Unary expr);

        R visitVariableExpr(Variable expr);

        R visitAssignExpr(Assign expr);

        R visitLogicalExpr(Logical expr);

        R visitCallExpr(Call expr);
    }

    public static class Binary extends Expr {
        public Binary(Expr left, Token operator, Expr right) {
            this.left = left;
            this.operator = operator;
            this.right = right;
        }

        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitBinaryExpr(this);
        }

        private final Expr left;
        private final Token operator;
        private final Expr right;

        public Token getOperator() {
            return operator;
        }

        public Expr getRight() {
            return right;
        }

        public Expr getLeft() {
            return left;
        }
    }

    public static class Grouping extends Expr {
        public Grouping(Expr expression) {
            this.expression = expression;
        }

        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitGroupingExpr(this);
        }

        private final Expr expression;

        public Expr getExpression() {
            return expression;
        }
    }

    public static class Literal extends Expr {
        public Literal(Object value) {
            this.value = value;
        }

        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitLiteralExpr(this);
        }

        private final Object value;

        public Object getValue() {
            return value;
        }
    }

    public static class Unary extends Expr {
        public Unary(Token operator, Expr right) {
            this.operator = operator;
            this.right = right;
        }

        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitUnaryExpr(this);
        }

        private final Token operator;
        private final Expr right;

        public Expr getRight() {
            return right;
        }

        public Token getOperator() {
            return operator;
        }
    }

    public static class Variable extends Expr {
        public Variable(Token name) {
            this.name = name;
        }

        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitVariableExpr(this);
        }

        private final Token name;

        public Token getName() {
            return name;
        }
    }

    public static class Assign extends Expr {
        public Assign(Token name, Expr value) {
            this.name = name;
            this.value = value;
        }

        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitAssignExpr(this);
        }

        private final Token name;

        public Token getName() {
            return name;
        }

        private final Expr value;

        public Expr getValue() {
            return value;
        }
    }

    public static class Logical extends Expr {
        public Logical(Expr left, Token operator, Expr right) {
            this.left = left;
            this.operator = operator;
            this.right = right;
        }

        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitLogicalExpr(this);
        }

        private final Expr left;
        private final Token operator;
        private final Expr right;

        public Expr getLeft() {
            return left;
        }

        public Expr getRight() {
            return right;
        }

        public Token getOperator() {
            return operator;
        }
    }

    public static class Call extends Expr {
        public Call(Expr callee, Token paren, ArrayList<Expr> arguments) {
            this.callee = callee;
            this.paren = paren;
            this.arguments = arguments;
        }

        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitCallExpr(this);
        }

        private final Expr callee;
        private final Token paren;
        private final ArrayList<Expr> arguments;

        public ArrayList<Expr> getArguments() {
            return arguments;
        }

        public Expr getCallee() {
            return callee;
        }

        public Token getParen() {
            return paren;
        }
    }


    public abstract <R> R accept(Visitor<R> visitor);
}
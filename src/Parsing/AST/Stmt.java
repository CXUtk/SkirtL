package Parsing.AST;

import Lexical.Token;

public abstract class Stmt {
    public interface Visitor<R> {
        R visitExpressionStmt(Expression stmt);
        R visitPrintStmt(Print stmt);
    }

    public static class Expression extends Stmt {
        public Expression(Expr expression) {
            this.expression = expression;
        }

        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitExpressionStmt(this);
        }

        private final Expr expression;

        public Expr getExpression() {
            return expression;
        }
    }

    public static class Print extends Stmt {
        public Print(Expr expression) {
            this.expression = expression;
        }

        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitPrintStmt(this);
        }

        private final Expr expression;

        public Expr getExpression() {
            return expression;
        }
    }

    public abstract <R> R accept(Visitor<R> visitor);
}

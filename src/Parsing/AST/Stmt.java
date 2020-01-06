package Parsing.AST;

import Lexical.Token;

import java.util.ArrayList;

public abstract class Stmt {
    public interface Visitor<R> {
        R visitExpressionStmt(Expression stmt);
        R visitPrintStmt(Print stmt);
        R visitVarStmt(Var stmt);
        R visitBlockStmt(Block stmt);
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

    public static class Var extends Stmt {
        public Var(Token name, Expr initializer) {
            this.name = name;
            this.initializer = initializer;
        }

        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitVarStmt(this);
        }

        private final Token name;
        private final Expr initializer;

        public Token getName() {
            return name;
        }

        public Expr getInitializer() {
            return initializer;
        }
    }

    public static class Block extends Stmt {
        public Block(ArrayList<Stmt> statements) {
            this.statements = statements;
        }

        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitBlockStmt(this);
        }

        private final ArrayList<Stmt> statements;

        public ArrayList<Stmt> getStatements() {
            return statements;
        }
    }

    public abstract <R> R accept(Visitor<R> visitor);
}

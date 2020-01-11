package Parsing.AST;

import Lexical.Token;

import java.util.ArrayList;

public abstract class Stmt {
    public interface Visitor<R> {
        R visitExpressionStmt(Expression stmt);
        R visitPrintStmt(Print stmt);
        R visitVarStmt(Var stmt);
        R visitBlockStmt(Block stmt);
        R visitIfStmt(If stmt);
        R visitWhileStmt(While stmt);
        R visitForStmt(For stmt);
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

    public static class If extends Stmt {
        public If(Expr condition, Block thenStmt, Block elseStmt) {
            this.condition = condition;
            this.thenBlock = thenStmt;
            this.elseBlock = elseStmt;
        }

        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitIfStmt(this);
        }

        private final Expr condition;
        private final Block thenBlock;
        private final Block elseBlock;

        public Expr getCondition() {
            return condition;
        }

        public Block getElseBlock() {
            return elseBlock;
        }

        public Block getThenBlock() {
            return thenBlock;
        }
    }

    public static class While extends Stmt {
        public While(Expr condition, Block body) {
            this.condition = condition;
            this.body = body;
        }

        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitWhileStmt(this);
        }

        private final Expr condition;
        private final Block body;

        public Expr getCondition() {
            return condition;
        }

        public Block getBody() {
            return body;
        }
    }

    public static class For extends Stmt {
        public For(Stmt decl, Expr condition, Expr upd, Block block) {
            this.decl = decl;
            this.condition = condition;
            this.upd = upd;
            this.block = block;
        }

        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitForStmt(this);
        }

        private final Stmt decl;
        private final Expr condition;
        private final Expr upd;
        private final Block block;

        public Expr getCondition() {
            return condition;
        }

        public Stmt getDecl() {
            return decl;
        }

        public Expr getUpd() {
            return upd;
        }

        public Block getBlock() {
            return block;
        }
    }

    public abstract <R> R accept(Visitor<R> visitor);
}

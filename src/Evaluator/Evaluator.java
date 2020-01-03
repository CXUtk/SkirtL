package Evaluator;

import ErrorHandling.RuntimeError;
import Lexical.Token;
import Parsing.AST.Expr;
import Parsing.AST.Stmt;

import java.util.ArrayList;

public class Evaluator implements Expr.Visitor<Object>, Stmt.Visitor<Object> {
    private Object evaluate(Expr expr){
        return expr.accept(this);
    }

    public void evaluate(ArrayList<Stmt> list){
        for(Stmt stmt : list){
            stmt.accept(this);
        }
    }
    @Override
    public Object visitBinaryExpr(Expr.Binary expr) {
        Object left = evaluate(expr.getLeft());
        Object right = evaluate(expr.getRight());
        switch (expr.getOperator().getType()) {
            case MINUS:
                return minus(left, right, expr.getOperator());
            case SLASH:
                return divide(left, right, expr.getOperator());
            case STAR:
                return multiply(left, right, expr.getOperator());
            case PLUS:
                return plus(left, right, expr.getOperator());
            case POWER:
                return power(left, right, expr.getOperator());
            case GREATER_THAN:
                return greater(left, right, expr.getOperator());
            case GREATER_THAN_EQUAL_TO:
                return greatereq(left, right, expr.getOperator());
            case LESS_THAN:
                return less(left, right, expr.getOperator());
            case LESS_THAN_EQUAL_TO:
                return lesseq(left, right, expr.getOperator());
            case EQUAL_TO:
                return equalTo(left, right);
            case NOT_EQUAL_TO:
                return !equalTo(left, right);
            default:
                break;
        }
        // Unreachable.
        return null;
    }

    private boolean equalTo(Object left, Object right) {
        if(left == null && right == null)return true;
        if(left == null)return false;
        return left.equals(right);
    }
    private Object greater(Object left, Object right, Token token) {
        if(left instanceof Long && right instanceof Long){
            return (long)left > (long)right;
        }
        else if(left instanceof Double && right instanceof Double){
            return (double)left > (double)right;
        }
        throw new RuntimeError("type between '>' operator does not match", token);
    }

    private Object greatereq(Object left, Object right, Token token) {
        if(left instanceof Long && right instanceof Long){
            return (long)left >= (long)right;
        }
        else if(left instanceof Double && right instanceof Double){
            return (double)left >= (double)right;
        }
        throw new RuntimeError("type between '>=' operator does not match", token);
    }

    private Object less(Object left, Object right, Token token) {
        if(left instanceof Long && right instanceof Long){
            return (long)left < (long)right;
        }
        else if(left instanceof Double && right instanceof Double){
            return (double)left < (double)right;
        }
        throw new RuntimeError("type between '<' operator does not match", token);
    }

    private Object lesseq(Object left, Object right, Token token) {
        if(left instanceof Long && right instanceof Long){
            return (long)left <= (long)right;
        }
        else if(left instanceof Double && right instanceof Double){
            return (double)left <= (double)right;
        }
        throw new RuntimeError("type between '<=' operator does not match", token);
    }

    private long power(long val, long exp){
        long ans = 1;
        while(exp > 0){
            if(exp % 2 == 1) ans *= val;
            val *= val;
            exp /= 2;
        }
        return ans;
    }

    private Object power(Object left, Object right, Token token) {
        if(left instanceof Long && right instanceof Long){
            return power((long)left, (long)right);
        }
        else if(left instanceof Double && right instanceof Double){
            return Math.pow((double)left, (double)right);
        }
        throw new RuntimeError("type between '^' operator does not match", token);
    }

    private Object minus(Object left, Object right, Token token) {
        if(left instanceof Long && right instanceof Long){
            return (long)left - (long)right;
        }
        else if(left instanceof Double && right instanceof Double){
            return (double)left - (double)right;
        }
        throw new RuntimeError("type between '-' operator does not match", token);
    }

    private Object plus(Object left, Object right, Token token) {
        if(left instanceof Long && right instanceof Long){
            return (long)left + (long)right;
        }
        else if(left instanceof Double && right instanceof Double){
            return (double)left + (double)right;
        }
        else if(left instanceof String && right instanceof String){
            return left + (String)right;
        }
        throw new RuntimeError("type between '+' operator does not match", token);
    }

    private Object multiply(Object left, Object right, Token token) {
        if(left instanceof Long && right instanceof Long){
            return (long)left * (long)right;
        }
        else if(left instanceof Double && right instanceof Double){
            return (double)left * (double)right;
        }
        throw new RuntimeError("type between '*' operator does not match", token);
    }

    private Object divide(Object left, Object right, Token token) {
        if(left instanceof Long && right instanceof Long){
            if((long)right == 0)
                throw new RuntimeError("division by zero", token);
            return (long)left / (long)right;
        }
        else if(left instanceof Double && right instanceof Double){
            return (double)left / (double)right;
        }
        throw new RuntimeError("type between '/' operator does not match", token);
    }


    @Override
    public Object visitGroupingExpr(Expr.Grouping expr) {
        return evaluate(expr.getExpression());
    }

    @Override
    public Object visitLiteralExpr(Expr.Literal expr) {
        return expr.getValue();
    }

    @Override
    public Object visitUnaryExpr(Expr.Unary expr) {
        Object right = evaluate(expr.getRight());
        switch (expr.getOperator().getType()) {
            case MINUS:
                return negate(right, expr.getOperator());
            case NOT:
                return !getTruth(right);
            case PLUS:
                // Useless plus sign
                return right;
            default:
                break;
        }
        return null;
    }

    private Object negate(Object right, Token token) {
        if (right instanceof Long)
            return -(long) right;
        else if (right instanceof Double)
            return -(double) right;
        else
            throw new RuntimeError("Cannot apply '-' to " + right.toString(), token);
    }


    private boolean getTruth(Object right) {
        if(right == null) return false;
        if(right instanceof Boolean) return (boolean)right;
        return true;
    }

    @Override
    public Object visitExpressionStmt(Stmt.Expression stmt) {
        evaluate(stmt.getExpression());
        return null;
    }

    @Override
    public Object visitPrintStmt(Stmt.Print stmt) {
        Object value = evaluate(stmt.getExpression());
        System.out.println(value.toString());
        return null;
    }

    @Override
    public Object visitVarStmt(Stmt.Var stmt) {
        return null;
    }
}

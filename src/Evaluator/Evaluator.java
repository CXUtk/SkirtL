package Evaluator;

import ErrorHandling.RuntimeError;
import Lexical.Token;
import Parsing.AST.Expr;

public class Evaluator implements Expr.Visitor<Object> {
    public Object evaluate(Expr expr){
        return expr.accept(this);
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
        }

        // Unreachable.
        return null;
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
        return null;
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
}

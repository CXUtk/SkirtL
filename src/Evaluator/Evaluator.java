package Evaluator;

import Parsing.AST.Expr;

public class Evaluator implements Expr.Visitor<Object> {

    private Object evaluate(Expr expr){
        return expr.accept(this);
    }

    @Override
    public Object visitBinaryExpr(Expr.Binary expr) {
        return null;
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
                return -(int) right;
            case NOT:
                return !getTruth(right);
            default:
                break;
        }
        return null;
    }

    private boolean getTruth(Object right) {
        if(right == null) return false;
        if(right instanceof Boolean) return (boolean)right;
        return true;
    }
}

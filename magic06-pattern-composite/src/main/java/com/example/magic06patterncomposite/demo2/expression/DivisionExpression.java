package com.example.magic06patterncomposite.demo2.expression;

/**
 * 减法表达式
 */
public class DivisionExpression extends BinaryOperatorExpression {

    public DivisionExpression(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public int getValue() {
        return left.getValue() / right.getValue();
    }
}

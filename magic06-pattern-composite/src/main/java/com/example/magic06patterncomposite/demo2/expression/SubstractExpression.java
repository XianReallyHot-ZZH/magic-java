package com.example.magic06patterncomposite.demo2.expression;

/**
 * 减法表达式
 */
public class SubstractExpression extends BinaryOperatorExpression {

    public SubstractExpression(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public int getValue() {
        return left.getValue() - right.getValue();
    }
}

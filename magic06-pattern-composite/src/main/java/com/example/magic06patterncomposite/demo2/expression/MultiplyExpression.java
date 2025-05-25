package com.example.magic06patterncomposite.demo2.expression;

/**
 * 乘法表达式
 */
public class MultiplyExpression extends BinaryOperatorExpression {

    public MultiplyExpression(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public int getValue() {
        return left.getValue() * right.getValue();
    }
}

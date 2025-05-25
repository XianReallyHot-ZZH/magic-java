package com.example.magic06patterncomposite.demo2.expression;

/**
 * 加法表达式
 */
public class AddExpression extends BinaryOperatorExpression {

    public AddExpression(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public int getValue() {
        return left.getValue() + right.getValue();
    }
}

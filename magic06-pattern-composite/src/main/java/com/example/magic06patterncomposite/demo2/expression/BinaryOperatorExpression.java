package com.example.magic06patterncomposite.demo2.expression;

public abstract class BinaryOperatorExpression implements Expression {

    Expression left;
    Expression right;

    protected BinaryOperatorExpression(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

}

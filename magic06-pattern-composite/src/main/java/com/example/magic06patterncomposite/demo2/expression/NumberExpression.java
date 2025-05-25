package com.example.magic06patterncomposite.demo2.expression;

/**
 * 数字表达式
 */
public class NumberExpression implements Expression {

    private final int value;

    public NumberExpression(int value) {
        this.value = value;
    }

    @Override
    public int getValue() {
        return value;
    }

}

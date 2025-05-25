package com.example.magic06patterncomposite.demo2;

public class Calc {

    public static void main(String[] args) {
        // 1+1 + 2+3
        Expression expression = new AddExpression(
                new AddExpression(
                        new NumberExpression(1),
                        new NumberExpression(1)
                ),
                new AddExpression(
                        new NumberExpression(2),
                        new NumberExpression(3)
                )
        );
        System.out.println(expression.getInt());
    }

    interface Expression {
        int getInt();
    }

    static class NumberExpression implements Expression {
        private int number;

        public NumberExpression(int number) {
            this.number = number;
        }

        @Override
        public int getInt() {
            return number;
        }
    }

    static class AddExpression implements Expression {
        private Expression left;
        private Expression right;

        public AddExpression(Expression left, Expression right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public int getInt() {
            return left.getInt() + right.getInt();
        }
    }

}

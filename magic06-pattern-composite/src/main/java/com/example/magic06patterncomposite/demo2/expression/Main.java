package com.example.magic06patterncomposite.demo2.expression;

public class Main {

    public static void main(String[] args) {
        // 1+15*(9+4+(1+5))+6
        ExpressionParser  parser = new ExpressionParser("1+15*(9+4+(1+5))+6");
        Expression expression = parser.parse();
        System.out.println(expression.getValue());
        System.out.println(1+15*(9+4+(1+5))+6);

        // 1+((2*3)+3)-10+1/2+(1+1)
        parser = new ExpressionParser("1+((2*3)+3)-10+1/2+(1+1)");
        expression = parser.parse();
        System.out.println(expression.getValue());
        System.out.println(1+((2*3)+3)-10+1/2+(1+1));
    }

}

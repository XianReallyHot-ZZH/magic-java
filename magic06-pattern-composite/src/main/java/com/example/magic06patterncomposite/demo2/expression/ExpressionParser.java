package com.example.magic06patterncomposite.demo2.expression;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * 表达式解析器
 * 1、完成对字符串表达式（中缀表达式），解析为后缀表达式（用一个栈承接）
 * 2、对后缀表达式进行计算，其实也就用Expression构建出树结构，得到最终的结果
 */
public class ExpressionParser {

    // 中缀表达式
    private final String infixExpression;

    // 指针,用于遍历中缀表达式
    private int point;

    public ExpressionParser(String infixExpression) {
        this.infixExpression = infixExpression;
    }

    /**
     * 转换为后缀表达式
     * <p>
     * 中缀表达式->后缀表达式，字符串解析规则：
     * 1、遇到操作数，直接push到结果
     * 2、遇到左括号（，入辅助栈
     * 3、遇到右括号），辅助栈顶元素push到结果，直到遇到左括号（，然后删除一对括号
     * 4、遇到运算符，让辅助栈顶优先级大于等于自己的运算符push到结果（相同等级的运算符，先入的优先级高），然后把自己入栈
     *
     * @return
     */
    public List<String> toSuffix() {
        List<String> suffix = new ArrayList<>();
        // 辅助栈
        LinkedList<String> stack = new LinkedList<>();

        while (point < infixExpression.length()) {
            char c = infixExpression.charAt(point);
            if (c == '(') { // 遇到左括号（，入辅助栈
                stack.addLast(c + "");
            } else if (c == ')') {  // 遇到右括号），辅助栈顶元素push到结果，直到遇到左括号（，然后删除一对括号
                while (!stack.getLast().equals("(")) {
                    suffix.add(stack.removeLast());
                }
                stack.removeLast();
            } else if (c == '*' || c == '/') {
                // 遇到运算符，让辅助栈顶优先级大于等于自己的运算符push到结果（相同等级的运算符，先入的优先级高），然后把自己入栈
                while (!stack.isEmpty() && (stack.getLast().equals("*") || stack.getLast().equals("/"))) {
                    suffix.add(stack.removeLast());
                }
                stack.addLast(c + "");
            } else if (c == '+' || c == '-') {
                // 遇到运算符，让辅助栈顶优先级大于等于自己的运算符push到结果（相同等级的运算符，先入的优先级高），然后把自己入栈
                while (topIsOperator(stack)) {
                    suffix.add(stack.removeLast());
                }
                stack.addLast(c + "");
            } else if (Character.isDigit(c)) {
                // 遇到操作数，直接push到结果
                StringBuilder sb = new StringBuilder();
                while (point < infixExpression.length() && Character.isDigit(infixExpression.charAt(point))) {
                    sb.append(infixExpression.charAt(point));
                    point++;
                }
                point--;
                suffix.add(sb.toString());
            } else {
                throw new IllegalStateException("非法字符!");
            }
            point++;
        }

        // 最后处理一下辅助栈中剩余的元素
        while (!stack.isEmpty()) {
            suffix.add(stack.removeLast());
        }

        return suffix;
    }

    /**
     * 构建组合出最终的表达式对象
     * 规则：
     *  依次遍历后缀表达式中的元素
     *  1、遇到数字时，直接构建一个数字表达式，入表达式辅助栈
     *  2、遇到操作符时，构建一个二元运算表达式，这时取出栈顶元素，就是右侧表达式，再取出栈顶元素，就是左侧表达式，然后将自身入栈
     *  3、最终的表达式辅助栈有且仅有一个元素，且就是最终的表达式对象
     *
     * @return
     */
    public Expression parse() {
        // 得到后缀表达式
        List<String> suffix = toSuffix();
        // 表达式辅助栈
        LinkedList<Expression> stack = new LinkedList<>();

        for (String token : suffix) {
            if (token.equals("+")) {
                Expression right = stack.removeLast();
                stack.addLast(new AddExpression(stack.removeLast(), right));
            } else if (token.equals("-")) {
                Expression right = stack.removeLast();
                stack.addLast(new SubstractExpression(stack.removeLast(), right));
            } else if (token.equals("*")) {
                Expression right = stack.removeLast();
                stack.addLast(new MultiplyExpression(stack.removeLast(), right));
            } else if (token.equals("/")) {
                Expression right = stack.removeLast();
                stack.addLast(new DivisionExpression(stack.removeLast(), right));
            } else {
                stack.addLast(new NumberExpression(Integer.parseInt(token)));
            }
        }

        return stack.removeLast();
    }

    private boolean topIsOperator(LinkedList<String> stack) {
        if (stack.isEmpty()) {
            return false;
        }
        return Set.of("*", "/", "+", "-").contains(stack.getLast());
    }

}

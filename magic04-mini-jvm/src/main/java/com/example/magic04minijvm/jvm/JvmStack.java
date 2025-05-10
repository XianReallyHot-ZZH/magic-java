package com.example.magic04minijvm.jvm;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * 线程中的那个线程栈
 */
public class JvmStack {

    // 线程栈
    private final Deque<StackFrame> stack = new ArrayDeque<>();

    // 判断栈是否为空
    public boolean isEmpty() {
        return stack.isEmpty();
    }

    // 获取当前栈顶的栈帧（不删除），为空时返回null
    public StackFrame peek() {
        return stack.peek();
    }

    // 获取当前栈顶的栈帧（并删除）,为空时抛出异常
    public StackFrame pop() {
        return stack.pop();
    }

    // 将栈帧压入栈顶
    public void push(StackFrame frame) {
        stack.push(frame);
    }


}

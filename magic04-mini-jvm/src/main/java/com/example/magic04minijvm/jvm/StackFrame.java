package com.example.magic04minijvm.jvm;

import tech.medivh.classpy.classfile.MethodInfo;
import tech.medivh.classpy.classfile.bytecode.Instruction;
import tech.medivh.classpy.classfile.constant.ConstantPool;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

/**
 * 栈帧：放置在线程栈中的具体对象。每一个栈帧其实就是一个方法执行的上下文。
 */
public class StackFrame {

    /**
     * 方法信息
     */
    final MethodInfo methodInfo;

    /**
     * 局部变量表
     */
    final Object[] localVariable;

    /**
     * 操作数栈
     */
    final Deque<Object> operandStack;

    /**
     * 指令
     */
    final List<Instruction> codes;

    /**
     * 常量池
     */
    final ConstantPool constantPool;

    /**
     * 指令索引,记录当前执行到那条指令了
     */
    int currentIndex;

    public StackFrame(MethodInfo methodInfo, ConstantPool constantPool, Object... args) {
        this.methodInfo = methodInfo;
        // 局部变量表的大小可以从方法信息中获取
        this.localVariable = new Object[methodInfo.getMaxLocals()];
        this.operandStack = new ArrayDeque<>();
        // 从方法信息中获取指令
        this.codes = methodInfo.getCodes();
        // 从方法信息中获取常量池
        this.constantPool = constantPool;
        System.arraycopy(args, 0, localVariable, 0, args.length);
    }

    /**
     * 获取下一条指令
     * @return
     */
    public Instruction getNextInstruction() {
        return codes.get(currentIndex++);
    }

    /**
     * 将对象压入当前栈帧的操作数栈
     * @param o
     */
    public void pushObjectToOperandStack(Object o) {
        operandStack.push(o);
    }

    public void jumpTo(int index) {
        for (int i = 0; i < codes.size(); i++) {
            Instruction instruction = codes.get(i);
            if (instruction.getPc() == index) {
                this.currentIndex = i;
                return;
            }
        }
    }

}

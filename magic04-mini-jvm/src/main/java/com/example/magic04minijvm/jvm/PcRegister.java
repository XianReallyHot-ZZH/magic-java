package com.example.magic04minijvm.jvm;

import tech.medivh.classpy.classfile.bytecode.Instruction;

import java.util.Iterator;

/**
 * pc寄存器,可以不断地拿到下一条指令,其实PC寄存器就是对线程栈从栈顶的栈帧中获取下一条指令的封装
 */
public class PcRegister implements Iterable<Instruction> {

    private final JvmStack jvmStack;

    public PcRegister(JvmStack jvmStack) {
        this.jvmStack = jvmStack;
    }

    @Override
    public Iterator<Instruction> iterator() {
        return new Itr();
    }

    class Itr implements Iterator<Instruction> {

        @Override
        public boolean hasNext() {
            // 线程栈中有栈帧，那么就有下一条指令
            return !jvmStack.isEmpty();
        }

        @Override
        public Instruction next() {
            return jvmStack.peek().getNextInstruction();
        }

    }


}

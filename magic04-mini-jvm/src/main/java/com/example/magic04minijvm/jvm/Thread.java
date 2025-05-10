package com.example.magic04minijvm.jvm;

import tech.medivh.classpy.classfile.ClassFile;
import tech.medivh.classpy.classfile.MethodInfo;
import tech.medivh.classpy.classfile.bytecode.*;
import tech.medivh.classpy.classfile.constant.ConstantMethodHandleInfo;
import tech.medivh.classpy.classfile.constant.ConstantMethodrefInfo;
import tech.medivh.classpy.classfile.constant.ConstantPool;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 虚拟机线程
 * JVM中方法的执行都会在一个具体的线程中
 */
public class Thread {

    private final String threadName;

    /**
     * 虚拟机栈
     */
    private final JvmStack stack;

    /**
     * 指令执行位置寄存器,可以通过它不断获取到下一条指令
     */
    private final PcRegister pcRegister;

    /**
     * 类加载器
     */
    private final BootstrapClassLoader classLoader;

    /**
     * 创建虚拟机线程
     *
     * @param threadName
     * @param stackFrame        线程创建时传入的栈帧（第一个要被执行的方法）
     */
    public Thread(String threadName, StackFrame stackFrame, BootstrapClassLoader classLoader) {
        this.threadName = threadName;
        this.stack = new JvmStack();
        this.stack.push(stackFrame);
        this.pcRegister = new PcRegister(stack);
        this.classLoader = classLoader;
    }

    /**
     * 启动虚拟机线程
     */
    public void start() throws Exception {

        // 从虚拟机栈中不断取出栈帧，执行栈帧中方法的指令，
        // 或者可以理解为从虚拟机栈中不断地取出指令进行执行，这里面我们需要记录一下上一次我们执行到哪个指令了
        for (Instruction instruction : pcRegister) {
            System.out.println("线程[" + threadName + "]正在执行指令：" + instruction);
            ConstantPool constantPool = stack.peek().constantPool;
            // 查阅java虚拟机字节码指令文档，实现指令的具体逻辑，以下为指令的简易版（玩具版实现），随便康康，不要较真
            switch (instruction.getOpcode()) {
                case getstatic -> {     // 获取静态变量
                    GetStatic getStatic = (GetStatic) instruction;
                    String className = getStatic.getClassName(constantPool);
                    String fieldName = getStatic.getFieldName(constantPool);
                    Object staticField;
                    // 理论上所有需要用到的类都需要在这里进行检测然后加载进来，这里面我们简单处理一下，因为我们本身就是一个java项目，所以java原生的类我们就直接用过反射获取了，不做加载了
                    if (className.contains("java")) {
                        Class<?> aClass = Class.forName(className);
                        Field declaredField = aClass.getDeclaredField(fieldName);
                        staticField = declaredField.get(null);
                        stack.peek().pushObjectToOperandStack(staticField);
                    }
                }
                case iconst_1 -> {
                    stack.peek().pushObjectToOperandStack(1);
                }
                case iconst_2 -> {
                    stack.peek().pushObjectToOperandStack(2);
                }
                case iconst_3 -> {
                    stack.peek().pushObjectToOperandStack(3);
                }
                case iconst_4 -> {
                    stack.peek().pushObjectToOperandStack(4);
                }
                case iload_0 -> {   // 将当前栈帧中的局部变量表中的第0号位的值压入操作数栈的最上面
                    stack.peek().pushObjectToOperandStack(stack.peek().localVariable[0]);
                }
                case iload_1 -> {
                    stack.peek().pushObjectToOperandStack(stack.peek().localVariable[1]);
                }
                case invokevirtual -> {  // 调用方法执行，简单处理，如果是java的原生方法，那么直接反射调用，如果是自定义的方法，那么创建栈帧，压入虚拟机栈中
                    InvokeVirtual invokeVirtual = (InvokeVirtual) instruction;
                    ConstantMethodrefInfo methodInfo = invokeVirtual.getMethodInfo(constantPool);
                    String className = methodInfo.className(constantPool);
                    String methodName = methodInfo.methodName(constantPool);
                    List<String> params = methodInfo.paramClassName(constantPool);
                    if (className.contains("java")) {
                        Class<?> aClass = Class.forName(className);
                        Method declaredMethod = aClass.getDeclaredMethod(methodName, params.stream().map(this::nameToClass).toArray(Class[]::new));
                        Object[] args = new Object[params.size()];
                        for (int index = args.length - 1; index >= 0; index--) {
                            args[index] = stack.peek().operandStack.pop();
                        }
                        Object result = declaredMethod.invoke(stack.peek().operandStack.pop(), args);
                        if (!methodInfo.isVoid(constantPool)) {
                            stack.peek().pushObjectToOperandStack(result);
                        }
                        continue;
                    }
                    ClassFile classFile = classLoader.load(className);
                    MethodInfo finalMethodInfo = classFile.getMethods(methodName).get(0);
                    Object[] args = new Object[params.size() + 1];
                    for (int index = args.length - 1; index >= 0; index--) {
                        args[index] = stack.peek().operandStack.pop();
                    }
                    StackFrame stackFrame = new StackFrame(finalMethodInfo, classFile.getConstantPool(), args);
                    stack.push(stackFrame);
                }
                case invokestatic -> {
                    InvokeStatic invokeStatic = (InvokeStatic) instruction;
                    ConstantMethodrefInfo methodInfo = invokeStatic.getMethodInfo(constantPool);
                    String className = methodInfo.className(constantPool);
                    String methodName = methodInfo.methodName(constantPool);
                    List<String> params = methodInfo.paramClassName(constantPool);
                    if (className.contains("java")) {
                        Class<?> aClass = Class.forName(className);
                        Method declaredMethod = aClass.getDeclaredMethod(methodName, params.stream().map(this::nameToClass).toArray(Class[]::new));
                        Object[] args = new Object[params.size()];
                        for (int index = args.length - 1; index >= 0; index--) {
                            args[index] = stack.peek().operandStack.pop();
                        }
                        Object result = declaredMethod.invoke(null, args);
                        if (!methodInfo.isVoid(constantPool)) {
                            stack.peek().pushObjectToOperandStack(result);
                        }
                        continue;
                    }
                    ClassFile classFile = classLoader.load(className);
                    MethodInfo finalMethodInfo = classFile.getMethods(methodName).get(0);
                    Object[] args = new Object[params.size()];
                    for (int index = args.length - 1; index >= 0; index--) {
                        args[index] = stack.peek().operandStack.pop();
                    }
                    StackFrame stackFrame = new StackFrame(finalMethodInfo, classFile.getConstantPool(), args);
                    stack.push(stackFrame);
                }
                case _return -> {
                    this.stack.pop();
                }
                case ireturn -> {
                    int result = (int) stack.peek().operandStack.pop();
                    stack.pop();
                    stack.peek().pushObjectToOperandStack(result);
                }
                case if_icmple -> {
                    int value2 = (int) stack.peek().operandStack.pop();
                    int value1 = (int) stack.peek().operandStack.pop();
                    if (value1 <= value2) {
                        Branch branch = (Branch) instruction;
                        int jumpTo = branch.getJumpTo();
                        stack.peek().jumpTo(jumpTo);
                    }
                }
                default -> {
                    throw new IllegalArgumentException("不支持的指令：" + instruction);
                }
            }
        }
    }

    // 这里简单处理一下，只处理一下int吧
    private Class<?> nameToClass(String className) {
        if (className.equals("int")) {
            return int.class;
        }
        try {
            return Class.forName(className);
        } catch (Exception e) {
            return null;
        }

    }

}

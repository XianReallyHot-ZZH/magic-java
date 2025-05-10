package com.example.magic04minijvm.jvm;

import tech.medivh.classpy.classfile.ClassFile;
import tech.medivh.classpy.classfile.ClassFileParser;
import tech.medivh.classpy.classfile.MethodInfo;

import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 模拟虚拟机Hotspot
 */
public class Hotspot {

    // 启动时的主类
    private String mainClass;

    private BootstrapClassLoader classLoader;

    /**
     * 构造函数
     * @param mainClass             主类，启动类
     * @param classPathString       类扫描路径
     */
    public Hotspot(String mainClass, String classPathString) {
        this.mainClass = mainClass;
        this.classLoader = new BootstrapClassLoader(Arrays.asList(classPathString.split(File.pathSeparator)));
    }

    /**
     * 虚拟机的启动入口
     * 职责：
     *  1、首先读取mainClass文件
     *  2、一行一行执行从mainClass中解析出的字节码文件，也就是执行具体的虚拟机指令
     */
    public void start() throws Exception {
        System.out.println("Hotspot start...");
        ClassFile mainClassFile = classLoader.load(mainClass);

        // 开始一行一行执行main方法对应的字节码指令，
        // 科普一下：jvm中执行一个方法是在一个线程中的，线程会拥有一个自己线程栈，首先先把要执行的方法压入栈顶（压入的其实是一个栈帧），然后执行方法体中的指令，
        // 如果指令依赖另一个方法，那么就再把依赖的方法（栈帧）压入栈顶，然后执行方法体中的指令，直到方法体中的指令执行完毕，再将此方法从栈顶弹出，
        // 然后继续往下执行原先方法体中的指令，直至所有方法执行结束，线程栈弹出所有方法（栈帧）
        StackFrame mainStackFrame = new StackFrame(mainClassFile.getMainMethod(), mainClassFile.getConstantPool());
        new Thread("main", mainStackFrame, classLoader).start();
    }

}

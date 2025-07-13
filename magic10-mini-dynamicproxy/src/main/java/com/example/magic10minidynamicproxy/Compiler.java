package com.example.magic10minidynamicproxy;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 动态编译器工具
 * 这个工具写的并不完整，但是没关系，不重要，仅供本次mini版组件使用
 */
public class Compiler {

    /**
     * 编译java文件
     *
     * @param javaFile
     */
    public static void compile(File javaFile) {

        // 获取编译器
        JavaCompiler compiler = javax.tools.ToolProvider.getSystemJavaCompiler();

        // 获取java文件管理器
        try (StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null)) {
            // 获取要编译的文件对象
//            Iterable<? extends JavaFileObject> compilableUnits = fileManager.getJavaFileObjects(javaFile);
            Iterable<? extends JavaFileObject> compilableUnits = fileManager.getJavaFileObjectsFromFiles(List.of(javaFile));

            // 设置编译选项
            List<String> options = List.of("-d", "./target/classes");   // 指定编译后的目录,编译出的class文件存放在target/classes目录下

            // 创建编译任务
            JavaCompiler.CompilationTask task = compiler.getTask(
                    null,
                    fileManager,
                    null,
                    options,
                    null,
                    compilableUnits);

            // 执行编译
            Boolean success = task.call();

            if (success) {
                System.out.println("编译成功");
            } else {
                System.out.println("编译失败");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

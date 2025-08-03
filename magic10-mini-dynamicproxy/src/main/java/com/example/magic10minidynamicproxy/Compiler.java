package com.example.magic10minidynamicproxy;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
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
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        // 获取java文件管理器
        try (StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null)) {
            // 获取要编译的文件对象
//            Iterable<? extends JavaFileObject> compilableUnits = fileManager.getJavaFileObjects(javaFile);
            Iterable<? extends JavaFileObject> compilableUnits = fileManager.getJavaFileObjectsFromFiles(List.of(javaFile));

            // 获取项目的类路径
//            String classpath = System.getProperty("java.class.path");

            // 获取项目根目录的绝对路径（由于是父子项目，需要手动添加子项目的编译结果路径到classpath下，不然会报找不到类），如果是正常的项目，这里不需要手动添加
            String projectDir = System.getProperty("user.dir") + File.separator + "magic10-mini-dynamicproxy";
            String targetClassesPath = projectDir + File.separator + "target" + File.separator + "classes";

            // 添加当前项目路径到类路径
            List<String> options = List.of(
                    "-d", targetClassesPath,            // 编译结果文件存放路径
                    "-cp", targetClassesPath       // 扫描类路径
            );

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
            throw new RuntimeException(e);
        }

    }

}

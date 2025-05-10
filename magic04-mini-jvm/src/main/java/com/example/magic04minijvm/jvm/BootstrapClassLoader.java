package com.example.magic04minijvm.jvm;

import tech.medivh.classpy.classfile.ClassFile;
import tech.medivh.classpy.classfile.ClassFileParser;

import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;

/**
 * 启动类加载器
 */
public class BootstrapClassLoader {

    // 启动时的类扫描路径，其实就是一个个文件夹路径
    private final List<String> classPath;

    public BootstrapClassLoader(List<String> classPath) {
        this.classPath = classPath;
    }

    /**
     * 加载获取指定类的classFile对象
     * @param fqcn 类的全限定名，如  java.lang.String
     * @return
     */
    public ClassFile load(String fqcn) throws ClassNotFoundException {
        return classPath.stream()
                // 尝试在每一个classPath中寻找mainClass的class文件，将class文件解析成一行一行的字节码
                .map(path -> tryLoad(path, fqcn))
                .filter(Objects::nonNull)
                .findAny()
                .orElseThrow(() -> new ClassNotFoundException(fqcn + " class not found"));
    }

    /**
     * 尝试加载指定路径下的指定的类
     * @param path
     * @param mainClass
     * @return
     */
    private ClassFile tryLoad(String path, String mainClass) {
        // 主类入参 a.b.c -> a/b/c.class
        File classFilePath = new File(path, mainClass.replace(".", File.separator) + ".class");
        if (!classFilePath.exists()) {
            return null;
        }
        try {
            byte[] bytes = Files.readAllBytes(classFilePath.toPath());
            return new ClassFileParser().parse(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}

package com.example.magic10minidynamicproxy;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.util.concurrent.atomic.AtomicInteger;

public class MiniProxy {

//    private static final String PROXY_PACKAGE = "com.mini.proxy";
    private static final String PROXY_PACKAGE = "com.example.magic10minidynamicproxy";
    private static final AtomicInteger COUNTER = new AtomicInteger(0);

    /**
     * 创建代理类对象
     *
     * @param loader            # 类加载器
     * @param interfaces        # 接口列表,支持多个接口
     * @param h                 # 调用处理器
     * @return
     * @throws Exception
     */
    public static Object newProxyInstance(ClassLoader loader,
                                          Class<?>[] interfaces,
                                          MiniInvocationHandler h) throws Exception {
        // 生成代理类的类名
        String className = generateClassName();
        // 创建代理类的符合java语法的.java文件
        File javaFile = createJavaFile(className, interfaces, h);
        // 编译代理类
        Compiler.compile(javaFile);

        // 加载编译出来的代理类
        Class<?> proxyClass = loader.loadClass(PROXY_PACKAGE + "." + className);
        // 获取构造方法
        Constructor<?> constructor = proxyClass.getConstructor(MiniInvocationHandler.class);
        // 创建代理类对象
        return constructor.newInstance(h);
    }

    /**
     * 生成代理类类名
     *
     * @return
     */
    private static String generateClassName() {
        return "Proxy$" + COUNTER.incrementAndGet();
    }

    /**
     * 创建代理类的.java文件
     * 其实很简单的：其实就是无聊的拼凑出一个符合java语法的.java文件，具体就是拼凑出各个接口的方法实现，在代理方法中完成对处理器的invoke方法调用
     *
     * @param className
     * @param interfaces
     * @param h
     * @return
     */
    private static File createJavaFile(String className, Class<?>[] interfaces, MiniInvocationHandler h) throws IOException {

        // 接口方法实现连接串，如：public class xxx implements i1,i2,i3，interfaceImplementations存的就是i1,i2,i3字符串
        StringBuilder interfaceImplementations = new StringBuilder();
        // 方法体拼凑存储器
        StringBuilder methods = new StringBuilder();
        // 收集需要导入的类
        StringBuilder imports = new StringBuilder();
        imports.append("import java.lang.reflect.Method;\n");
        imports.append("import com.example.magic10minidynamicproxy.MiniInvocationHandler;\n");
        // 添加接口类的导入
        for (Class<?> iface : interfaces) {
            imports.append("import ").append(iface.getCanonicalName()).append(";\n");
        }

        // 遍历接口
        for (Class<?> iface : interfaces) {
            interfaceImplementations.append(iface.getSimpleName()).append(", ");
            // 遍历接口方法
            for (Method method : iface.getMethods()) {
                // 方法参数签名部分
                StringBuilder params = new StringBuilder();
                // 方法参数名称部分
                StringBuilder paramNames = new StringBuilder();
                Class<?>[] parameterTypes = method.getParameterTypes();
                for (int i = 0; i < parameterTypes.length; i++) {
                    // 拼凑参数签名
                    params.append(parameterTypes[i].getName()).append(" arg").append(i);
                    // 拼凑参数名称
                    paramNames.append("arg").append(i);
                    if (i < parameterTypes.length - 1) {
                        params.append(", ");
                        paramNames.append(", ");
                    }
                }

                // 开始拼凑方法体
                methods.append("    @Override\n");
                methods.append("    public ").append(method.getReturnType().getName()).append(" ").append(method.getName()).append("(").append(params).append(") {\n");
                methods.append("        try {\n");
                methods.append("            Method method = ").append(iface.getSimpleName()).append(".class.getMethod(\"").append(method.getName()).append("\"");
                if (parameterTypes.length > 0) {
                    methods.append(", ");
                    for (int i = 0; i < parameterTypes.length; i++) {
                        methods.append(parameterTypes[i].getName()).append(".class");
                        if (i < parameterTypes.length - 1) {
                            methods.append(", ");
                        }
                    }
                }
                methods.append(");\n");
                methods.append("            Object[] args = new Object[]{").append(paramNames).append("};\n");
                if (!method.getReturnType().equals(void.class)) {
                    methods.append("            return (").append(method.getReturnType().getName()).append(") ");
                }
                methods.append("            this.h.invoke(this, method, args);\n");
                methods.append("        } catch (Throwable e) {\n");
                methods.append("            throw new RuntimeException(e);\n");
                methods.append("        }\n");
                methods.append("    }\n");
                methods.append("\n");
            }
        }

        if (!interfaceImplementations.isEmpty()) {
            interfaceImplementations.setLength(interfaceImplementations.length() - 2);
        }

        String context = "package " + PROXY_PACKAGE + ";\n" +
                "\n" +
                imports.toString() +
                "\n" +
                "public class " + className + " implements " + interfaceImplementations.toString() + " {\n" +
                "    private MiniInvocationHandler h;\n" +
                "\n" +
                "    public " + className + "(MiniInvocationHandler h) {\n" +
                "        this.h = h;\n" +
                "    }\n" +
                "\n" +
                methods.toString() +
                "}\n";

        // 创建包目录结构
        File packageDir = new File("target/classes/" + PROXY_PACKAGE.replace(".", "/"));
        if (!packageDir.exists()) {
            packageDir.mkdirs();
        }

        File javaFile = new File("target/classes/" + PROXY_PACKAGE.replace(".", "/"), className + ".java");
        Files.writeString(javaFile.toPath(), context);
        return javaFile;
    }


}

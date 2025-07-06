package com.example.magic09minispring;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 模拟Spring IoC 容器
 */
public class ApplicationContext {

    private Map<String/*对象的名字*/, Object/*对象的实例*/> ioc = new HashMap<>();
    private Map<String/*对象的名字*/, Object/*还未完整加载完的实例对象*/> loadingIoc = new HashMap<>();
    private Map<String/*对象名字*/, BeanDefinition/*对象定义*/> beanDefinitionMap = new HashMap<>();

    public ApplicationContext(String packageName) throws Exception {
        initContext(packageName);
    }

    public Object getBean(String beanName) {
        if (beanName == null) {
            return null;
        }
        // ioc中已经有实例了，那么直接返回
        Object bean = ioc.get(beanName);
        if (bean !=  null) {
            return bean;
        }
        // 还没创建，那么创建bean
        if (beanDefinitionMap.containsKey(beanName)) {
            return createBean(beanDefinitionMap.get(beanName));
        }
        // 走到这，说明beanName不存在
        return null;
    }

    public <T> T getBean(Class<T> beanType) {
        String beanName = this.beanDefinitionMap.values().stream()
                .filter(beanDefinition -> beanType.isAssignableFrom(beanDefinition.getBeanType()))
                .map(BeanDefinition::getName)
                .findFirst()
                .orElse(null);
        return (T) getBean(beanName);
    }

    public <T> List<T> getBeans(Class<T> beanType) {
        return this.beanDefinitionMap.values().stream()
                .filter(beanDefinition -> beanType.isAssignableFrom(beanDefinition.getBeanType()))
                .map(BeanDefinition::getName)
                .map(this::getBean)
                .map(bean -> (T) bean)
                .collect(Collectors.toList());
    }

    /**
     * 初始化容器,造对象的入口
     * 步骤：
     * 1、扫描找到要造哪些对象（解决造哪些的问题）
     * 2、根据对象信息，创建对象（解决具体对象怎么造的问题）
     * （1）构造函数、入参
     * （2）成员变量的自动注入
     * （3）对象生命周期函数调用
     * （4）整个初始化流程中的扩展点
     * （5）。。。
     * <p>
     * 简单版本：先实现支持一个包的扫描路径，然后根据包名，进行扫描，并创建对象
     *
     * @param packageName
     */
    public void initContext(String packageName) throws Exception {
        List<BeanDefinition> beanDefinitions = scanPackage(packageName).stream()
                .filter(this::needCreate)
                .map(this::wrapperBeanDefinition)
                .toList();
        beanDefinitions.forEach(this::createBean);
    }

    /**
     * 进阶版：支持多个包的扫描路径
     *
     * @param packageNames
     */
    public void initContext(String... packageNames) {

    }

    /**
     * 扫描指定的包，返回所有类
     *
     * @param packageName
     * @return
     */
    private List<Class<?>> scanPackage(String packageName) throws Exception {
        List<Class<?>> classeList = new ArrayList<>();
        // 入参进行转化 a.b.c -> a/b/c（linux） a\b\c（windows）
        URL resource = this.getClass().getClassLoader().getResource(packageName.replace(".", File.separator));
//        URL resource = ClassLoader.getSystemResource(packageName.replace(".", File.separator));

        Files.walkFileTree(Path.of(resource.toURI()), new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                // 处理class文件
                Path absolutePath = file.toAbsolutePath();
                if (absolutePath.toString().endsWith(".class")) {
//                    System.out.println(absolutePath);
                    String replaceStr = absolutePath.toString().replace(File.separator, ".");
                    int packageIndex = replaceStr.indexOf(packageName);
                    String className = replaceStr.substring(packageIndex, replaceStr.length() - ".class".length());
//                    System.out.println(className);
                    try {
                        classeList.add(Class.forName(className));
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
                // 每次处理完一个文件都继续扫描处理下一个
                return FileVisitResult.CONTINUE;
            }
        });
        return classeList;
    }

    /**
     * 判定类是否需要被容器创建
     *
     * @param aClass
     * @return
     */
    protected boolean needCreate(Class<?> aClass) {
        return aClass.isAnnotationPresent(Component.class);
    }

    /**
     * 封装成BeanDefinition
     *
     * @param aClass
     * @return
     */
    private BeanDefinition wrapperBeanDefinition(Class<?> aClass) {
        BeanDefinition beanDefinition = new BeanDefinition(aClass);
        // fix: 解决bean名称有可能重复的问题
        if (beanDefinitionMap.containsKey(beanDefinition.getName())) {
            throw new RuntimeException("bean name is exists:" + beanDefinition.getName());
        }
        beanDefinitionMap.put(beanDefinition.getName(), beanDefinition);
        return beanDefinition;
    }

    /**
     * 创建Bean
     *
     * @param beanDefinition
     */
    protected Object createBean(BeanDefinition beanDefinition) {
        // 判断实例IOC容器中是否有这个对象
        if (ioc.containsKey(beanDefinition.getName())) {
            return ioc.get(beanDefinition.getName());
        }
        // 判断loadingIOC容器中，是否有这个对象
        if (loadingIoc.containsKey(beanDefinition.getName())) {
            return loadingIoc.get(beanDefinition.getName());
        }
        return doCreateBean(beanDefinition);
    }

    /**
     * 创建Bean的具体实现
     *
     * @param beanDefinition
     */
    private Object doCreateBean(BeanDefinition beanDefinition) {
        Object bean = null;
        try {
            bean = beanDefinition.getConstructor().newInstance();
            // 把自己放进loadingIoc中，因为这时候该bean还没有完全加载完
            loadingIoc.put(beanDefinition.getName(), bean);
            // 实现对成员变量的注入
            autowiredBean(bean, beanDefinition);
            // 实现对@PostConstruct注解的方法调用
            Method postConstructMethod = beanDefinition.getPostConstructMethod();
            if (postConstructMethod != null) {
                postConstructMethod.invoke(bean);
            }
            // 走到这步，说明bean已经完整的完成实例化加载，可以从loadingIoc中取出放入真正的ioc容器中了
            ioc.put(beanDefinition.getName(), loadingIoc.remove(beanDefinition.getName()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return bean;
    }

    /**
     * 实现对bean的属性依赖自动注入
     *
     * @param bean
     * @param beanDefinition
     */
    private void autowiredBean(Object bean, BeanDefinition beanDefinition) throws IllegalAccessException {
        for (Field autowiredField : beanDefinition.getAutowiredFields()) {
            autowiredField.setAccessible(true);
            // 根据属性类型获取bean
            autowiredField.set(bean, getBean(autowiredField.getType()));
        }
    }


}

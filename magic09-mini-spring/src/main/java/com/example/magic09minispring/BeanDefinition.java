package com.example.magic09minispring;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * BeanDefinition,bean元信息
 */
public class BeanDefinition {

    private String name;
    // 默认构造函数
    private Constructor<?> constructor;

    // 构建后的初始化方法(这一版简单实现，默认只有一个初始化方案)
    private Method postConstructMethod;

    // 进阶版：多个初始化方法
    private Method[] postConstructMethods;

    public BeanDefinition(Class<?> type) {
        Component component = type.getDeclaredAnnotation(Component.class);
        // 获取bean名称, 默认为类名, 如果注解中指定了名称，则使用指定的名称
        this.name = component.name().isEmpty() ? type.getSimpleName() : component.name();
        try {
            this.constructor = type.getConstructor();
            postConstructMethod = Arrays.stream(type.getDeclaredMethods())
                    .filter(method -> method.isAnnotationPresent(PostConstruct.class))
                    .findFirst()
                    .orElse(null);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public String getName() {
        return name;
    }

    public Constructor<?> getConstructor() {
        return constructor;
    }

    public Method getPostConstructMethod() {
        return postConstructMethod;
    }

    public Method[] getPostConstructMethods() {
        return postConstructMethods;
    }

}

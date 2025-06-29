package com.example.magic09minispring;

import java.lang.reflect.Constructor;

/**
 * BeanDefinition,bean元信息
 */
public class BeanDefinition {

    private String name;
    // 默认构造函数
    private Constructor<?> constructor;

    public BeanDefinition(Class<?> type) {
        Component component = type.getDeclaredAnnotation(Component.class);
        // 获取bean名称, 默认为类名, 如果注解中指定了名称，则使用指定的名称
        this.name = component.name().isEmpty() ? type.getSimpleName() : component.name();
        try {
            this.constructor = type.getConstructor();
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

}

package com.example.magic09minispring;

import java.util.List;

/**
 * 模拟Spring IoC 容器
 */
public class ApplicationContext {

    public ApplicationContext(String packageName) {
        initContext(packageName);
    }

    public Object getBean(String beanName) {
        return null;
    }

    public <T> T getBean(Class<T> beanType) {
        return null;
    }

    public <T> List<T> getBeans(Class<T> beanType) {
        return null;
    }

    /**
     * 初始化容器,造对象的入口
     * 步骤：
     * 1、扫描找到要造哪些对象（解决造哪些的问题）
     * 2、根据对象信息，创建对象（解决具体对象怎么造的问题）
     *   （1）构造函数、入参
     *   （2）成员变量的注入
     *   （3）对象生命周期函数调用
     *   （4）整个初始化流程中的扩展点
     *   （5）。。。
     * <p>
     * 简单版本：先实现支持一个包的扫描路径，然后根据包名，进行扫描，并创建对象
     * @param packageName
     */
    public void initContext(String packageName) {
        List<BeanDefinition> beanDefinitions = scanPackage(packageName).stream()
                .filter(this::needCreate)
                .map(this::wrapperBeanDefinition)
                .toList();
        beanDefinitions.forEach(this::createBean);
    }

    /**
     * 进阶版：支持多个包的扫描路径
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
    private List<Class<?>> scanPackage(String packageName) {
        return null;
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
        return new BeanDefinition(aClass);
    }

    /**
     * 创建Bean
     *
     * @param beanDefinition
     */
    protected void createBean(BeanDefinition beanDefinition) {

    }


}

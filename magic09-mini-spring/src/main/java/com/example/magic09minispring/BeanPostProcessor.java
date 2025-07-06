package com.example.magic09minispring;

/**
 * BeanPostProcessor: bean后置处理器,在bean的初始化阶段对每一个bean进行干预
 */
public interface BeanPostProcessor {

    default Object beforeInitializeBean(Object bean, String beanName) {
        return bean;
    }

    default Object afterInitializeBean(Object bean, String beanName) {
        return bean;
    }

}



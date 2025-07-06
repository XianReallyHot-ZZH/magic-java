package com.example.magic09minispring.sub;

import com.example.magic09minispring.BeanPostProcessor;
import com.example.magic09minispring.Component;

@Component
public class MyBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object beforeInitializeBean(Object bean, String beanName) {
        System.out.println("beforeInitializeBean: " + beanName);
        return bean;
    }

    @Override
    public Object afterInitializeBean(Object bean, String beanName) {
        System.out.println("afterInitializeBean: " + beanName);
        return bean;
    }

}

package com.example.magic09minispring;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注解，标识哪些类需要被IOC管理
 * 规定：添加了@Component注解的类，会被IOC容器管理
 * 帮助解决容器要造哪些对象、管理哪些对象的问题
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Component {



}

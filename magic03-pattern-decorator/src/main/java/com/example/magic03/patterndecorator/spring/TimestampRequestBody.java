package com.example.magic03.patterndecorator.spring;

import java.lang.annotation.*;

/**
 * RequestBody注解的增强，当入参是一个Map的时候，那么会对入参map自动添加一个timestamp字段
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TimestampRequestBody {
}

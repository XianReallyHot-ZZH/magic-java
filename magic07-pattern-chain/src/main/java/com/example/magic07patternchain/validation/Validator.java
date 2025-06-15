package com.example.magic07patternchain.validation;

import com.example.magic07patternchain.annotation.Length;
import com.example.magic07patternchain.annotation.Max;
import com.example.magic07patternchain.annotation.Min;
import com.example.magic07patternchain.exception.ValidatorException;

import java.lang.reflect.Field;

public class Validator {

    /**
     * 校验bean，抛出异常则校验失败，否则校验成功
     *
     * @param bean
     * @throws ValidatorException
     */
    public void validate(Object bean) throws ValidatorException, IllegalAccessException {
        Class<?> beanClass = bean.getClass();
        for (Field field : beanClass.getDeclaredFields()) {
            field.setAccessible(true);
            ValidatorChain validatorChain = buildValidatorChain(field);
            validatorChain.validate(field.get(bean));
        }
    }

    /**
     * 构建校验链
     *
     * @param field     成员对象
     * @return
     */
    private ValidatorChain buildValidatorChain(Field field) {
        ValidatorChain chain = new ValidatorChain();
        Max max = field.getAnnotation(Max.class);
        if (max != null) {
            chain.addLastHandler(new MaxValidatorHandler(max.value()));
        }
        Min  min = field.getAnnotation(Min.class);
        if (min != null) {
            chain.addLastHandler(new MinValidatorHandler(min.value()));
        }
        Length length = field.getAnnotation(Length.class);
        if (length != null) {
            chain.addLastHandler(new LengthValidatorHandler(length.value()));
        }
        return chain;
    }


}

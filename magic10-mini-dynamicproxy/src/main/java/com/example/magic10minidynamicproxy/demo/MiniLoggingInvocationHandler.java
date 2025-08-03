package com.example.magic10minidynamicproxy.demo;

import com.example.magic10minidynamicproxy.MiniInvocationHandler;

import java.lang.reflect.Method;

public class MiniLoggingInvocationHandler implements MiniInvocationHandler {
    private final Object target;

    public MiniLoggingInvocationHandler(Object target) {
        this.target = target;
    }

    /**
     * 自定义逻辑处理，所有代理对象的方法调用都会经过 invoke 方法。
     *
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("调用方法: " + method.getName() + " 开始");
        Object result = method.invoke(target, args);
        System.out.println("调用方法: " + method.getName() + " 结束，结果为: " + result);
        return result;
    }
}

package com.example.magic10minidynamicproxy;

import java.lang.reflect.Method;

public interface MiniInvocationHandler {

    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable;

}

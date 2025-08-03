package com.example.magic10minidynamicproxy.demo;

import java.lang.reflect.Proxy;

public class Main {
    public static void main(String[] args) {
        // 创建目标对象
        Calculator calculator = new CalculatorImpl();

        // 用于创建代理实例，需要传入类加载器、接口数组和调用处理器
        Calculator proxyInstance = (Calculator) Proxy.newProxyInstance(
                calculator.getClass().getClassLoader(), // 类加载器
                calculator.getClass().getInterfaces(),  // 接口列表
                new LoggingInvocationHandler(calculator) // 调用处理器
        );

        // 通过代理对象调用方法
        int result = proxyInstance.add(3, 5);
        System.out.println("最终结果: " + result);
    }
}

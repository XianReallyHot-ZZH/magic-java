package com.example.magic10minidynamicproxy.demo;

import com.example.magic10minidynamicproxy.MiniProxy;

import java.lang.reflect.Proxy;

public class Main {
    public static void main(String[] args) throws Exception {
        // 创建目标对象
        Calculator calculator = new CalculatorImpl();

        // 用于创建代理实例，需要传入类加载器、接口数组和调用处理器
        Object proxyInstance = Proxy.newProxyInstance(
                calculator.getClass().getClassLoader(), // 类加载器
                calculator.getClass().getInterfaces(),  // 接口列表
                new LoggingInvocationHandler(calculator) // 调用处理器
        );

        // 通过代理对象调用方法
        int result = ((Calculator) proxyInstance).add(3, 5);
        System.out.println("加法最终结果: " + result);
        result = ((Calculator) proxyInstance).sub(10, 5);
        System.out.println("减法最终结果: " + result);
        result = ((CalculatorM) proxyInstance).mul(3, 5);
        System.out.println("乘法最终结果: " + result);
        result = ((CalculatorM) proxyInstance).div(10, 5);
        System.out.println("除法最终结果: " + result);



        System.out.println("============================分割线============================");

        Object proxyInstance2 = MiniProxy.newProxyInstance(
                calculator.getClass().getClassLoader(), // 类加载器
                calculator.getClass().getInterfaces(),  // 接口列表
                new MiniLoggingInvocationHandler(calculator) // 调用处理器
        );
        int result2 = ((Calculator) proxyInstance2).add(3, 5);
        System.out.println("加法最终结果: " + result2);
        result2 = ((Calculator) proxyInstance2).sub(10, 5);
        System.out.println("减法最终结果: " + result2);
        result2 = ((CalculatorM) proxyInstance2).mul(3, 5);
        System.out.println("乘法最终结果: " + result2);
        result2 = ((CalculatorM) proxyInstance2).div(10, 5);
        System.out.println("除法最终结果: " + result2);
    }
}

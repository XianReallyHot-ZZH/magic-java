package com.example.magic03.patterndecorator.set;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        // 使用装饰器设计模式，可以实现任意的组合与修饰，比继承更灵活
        Set<String> historySet = new HistorySet<>(new HashSet<>());
        Set<String> set = new HistorySet<>(historySet);
        set.add("1");
        set.add("2");
        set.add("3");
        set.add("4");
        set.remove("4");
        set.remove("4");
        set.remove("5");
        set.remove("1");
        System.out.println(set);

        // 这是jdk里利用的装饰器设计模式的一个例子：实现对集合对象的线程安全包装
        Collection<Object> objects = Collections.synchronizedCollection(new ArrayList<>());
        if (objects.isEmpty()) {
            objects.add(1);
        }
    }

}

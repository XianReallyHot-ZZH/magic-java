package com.example.magic05patterniterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<User> userList = new ArrayList<>();
        User tom = new User("Tom", 11);
        User jerry = new User("Jerry", 11);
        userList.add(tom);
        userList.add(jerry);
        for (User user : userList) {
            System.out.println(user);
        }

        // 通过将for语句编译成字节码，观察字节码，上面for语法糖其实对应的就是下面这一段代码
        Iterator<User> iterator = userList.iterator();
        while (iterator.hasNext()) {
            User user = iterator.next();
            System.out.println(user);
        }


    }

}

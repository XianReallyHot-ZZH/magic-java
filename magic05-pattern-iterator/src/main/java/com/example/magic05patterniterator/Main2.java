package com.example.magic05patterniterator;

import java.util.Iterator;

public class Main2 {

    public static void main(String[] args) {
        UserIterable userIterable = new UserIterable("Tom", 11);
        for (String s : userIterable) {
            System.out.println(s);
        }

        Iterator<String> iterator = userIterable.iterator();
        while (iterator.hasNext()) {
            String s = iterator.next();
            System.out.println(s);
        }
    }

}

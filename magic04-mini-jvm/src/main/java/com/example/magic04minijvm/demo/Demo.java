package com.example.magic04minijvm.demo;

public class Demo {

    public static void main(String[] args) {
        // case1
        System.out.println(1);

        // case2
        System.out.println(2);

        // case3
        System.out.println(max(1, 4));

        // case4
        System.out.println(max(3, 1));
    }


    public static int max(int a, int b) {
        if (a > b) {
            return a;
        }
        return b;
    }

}

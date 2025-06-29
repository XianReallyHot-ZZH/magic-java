package com.example.magic09minispring;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws Exception {
        ApplicationContext ioc = new ApplicationContext("com.example.magic09minispring");
        System.out.println(ioc.getBean("Cat"));
        System.out.println(ioc.getBean("mydog"));
    }

}

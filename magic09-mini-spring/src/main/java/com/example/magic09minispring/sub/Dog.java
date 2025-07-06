package com.example.magic09minispring.sub;

import com.example.magic09minispring.Autowired;
import com.example.magic09minispring.Component;
import com.example.magic09minispring.PostConstruct;

@Component(name = "mydog")
public class Dog {

    @Autowired
    Cat cat;

    @PostConstruct
    public void init() {
        System.out.println("Dog 创建成功，mydog里有一个属性" + cat);
    }

}

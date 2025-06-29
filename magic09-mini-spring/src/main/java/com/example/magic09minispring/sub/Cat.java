package com.example.magic09minispring.sub;

import com.example.magic09minispring.Autowired;
import com.example.magic09minispring.Component;
import com.example.magic09minispring.PostConstruct;

@Component
public class Cat {

    @Autowired
    private Dog dog;

    @PostConstruct
    public void init() {
        System.out.println("Cat 创建成功，cat里有一个属性" + dog);
    }

}

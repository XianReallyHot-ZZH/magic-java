package com.example.magic07patternchain;

import com.example.magic07patternchain.annotation.Length;
import com.example.magic07patternchain.annotation.Max;
import com.example.magic07patternchain.annotation.Min;

public class User {

    @Length(value = 3)
    private final String name;

    @Max(value = 17)
    @Min(value = 20)
    private final Integer age;

    public User(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

}

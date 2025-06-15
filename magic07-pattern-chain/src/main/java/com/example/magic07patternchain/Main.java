package com.example.magic07patternchain;

import com.example.magic07patternchain.validation.Validator;

public class Main {

    public static void main(String[] args) throws IllegalAccessException {
        User user = new User("Tom", 18);
        Validator validator = new Validator();
        validator.validate(user);
    }

}

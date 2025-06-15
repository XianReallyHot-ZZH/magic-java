package com.example.magic07patternchain.validation;

import com.example.magic07patternchain.exception.ValidatorException;

public class MaxValidatorHandler implements ValidatorHandler {

    private final int max;

    public MaxValidatorHandler(int max) {
        this.max = max;
    }

    @Override
    public void validate(Object value) {
        if (value instanceof Integer intValue) {
            if (intValue > max) {
                throw new ValidatorException("Max value is " + max + " but got " + intValue);
            }
        }
    }

}

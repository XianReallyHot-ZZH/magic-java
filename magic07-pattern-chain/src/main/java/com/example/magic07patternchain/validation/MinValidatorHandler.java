package com.example.magic07patternchain.validation;

import com.example.magic07patternchain.exception.ValidatorException;

public class MinValidatorHandler implements ValidatorHandler {

    private final int min;

    public MinValidatorHandler(int min) {
        this.min = min;
    }

    @Override
    public void validate(Object value) {
        if (value instanceof Integer intValue) {
            if (intValue < min) {
                throw new ValidatorException("Min value is " + min + " but got " + intValue);
            }
        }
    }

}

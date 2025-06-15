package com.example.magic07patternchain.validation;

import com.example.magic07patternchain.exception.ValidatorException;

public class LengthValidatorHandler implements ValidatorHandler {

    private final int length;

    public LengthValidatorHandler(int length) {
        this.length = length;
    }

    @Override
    public void validate(Object value) {
        if (value instanceof String stringValue) {
            if (stringValue.length() !=  length) {
                throw new ValidatorException("Length is " + length + " but got " + stringValue.length());
            }
        }
    }

}

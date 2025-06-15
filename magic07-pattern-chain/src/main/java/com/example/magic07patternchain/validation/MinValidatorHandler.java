package com.example.magic07patternchain.validation;

public class MinValidatorHandler implements ValidatorHandler {

    private final int min;

    public MinValidatorHandler(int min) {
        this.min = min;
    }

    @Override
    public void validate(Object value, ValidatorContext context) {
        if (value instanceof Integer intValue) {
            if (intValue < min) {
                context.addErrorMessage("Min value is " + min + " but got " + intValue);
            }
        }
    }

}

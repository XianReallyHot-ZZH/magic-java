package com.example.magic07patternchain.validation;

public class MaxValidatorHandler implements ValidatorHandler {

    private final int max;

    public MaxValidatorHandler(int max) {
        this.max = max;
    }

    @Override
    public void validate(Object value, ValidatorContext context) {
        if (value instanceof Integer intValue) {
            if (intValue > max) {
                context.addErrorMessage("Max value is " + max + " but got " + intValue);
            }
        }
    }
}

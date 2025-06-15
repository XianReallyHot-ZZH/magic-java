package com.example.magic07patternchain.validation;

public class MinValidatorHandler implements ValidatorHandler {

    private final int min;

    public MinValidatorHandler(int min) {
        this.min = min;
    }

    @Override
    public void validate(Object value, ValidatorContext context) {
        Object name = context.get("name");
        if (name != null) {
            System.out.println("之前由" + name + "校验过");
        }
        if (value instanceof Integer intValue) {
            if (intValue < min) {
                context.addErrorMessage("Min value is " + min + " but got " + intValue);
            }
        }
        context.doNext(value);
    }

}

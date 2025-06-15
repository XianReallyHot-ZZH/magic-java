package com.example.magic07patternchain.validation;

public class LengthValidatorHandler implements ValidatorHandler {

    private final int length;

    public LengthValidatorHandler(int length) {
        this.length = length;
    }

    @Override
    public void validate(Object value, ValidatorContext context) {
        if (value instanceof String stringValue) {
            if (stringValue.length() != length) {
                context.addErrorMessage("Length is " + length + " but got " + stringValue.length());
            }
        }
    }

}

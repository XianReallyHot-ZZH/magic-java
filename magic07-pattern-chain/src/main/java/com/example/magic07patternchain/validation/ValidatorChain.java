package com.example.magic07patternchain.validation;

import com.example.magic07patternchain.exception.ValidatorException;

import java.util.ArrayList;
import java.util.List;

public class ValidatorChain {

    private final List<ValidatorHandler> handlers = new ArrayList<>();

    public void validate(Object value) throws ValidatorException {
        ValidatorContext context = new ValidatorContext();
        for (ValidatorHandler handler : handlers) {
            handler.validate(value, context);
        }
        context.throwExceptionIfHasError();
    }


    public void addLastHandler(ValidatorHandler handler) {
        handlers.add(handler);
    }


}

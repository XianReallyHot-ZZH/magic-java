package com.example.magic07patternchain.validation;

import com.example.magic07patternchain.exception.ValidatorException;

import java.util.ArrayList;
import java.util.List;

public class ValidatorChain {

    private final List<ValidatorHandler> handlers = new ArrayList<>();

    public void validate(Object value) throws ValidatorException {
        ValidatorContext context = new ValidatorContext(value);
        while (true) {
            int index = context.currentIndex();
            if (index >= handlers.size()) {
                break;
            }
            handlers.get(index).validate(context.getValue(), context);
            if (context.currentIndex() == index) {
                // 说明没有调用doNext
                break;
            }
        }
        context.throwExceptionIfHasError();
    }


    public void addLastHandler(ValidatorHandler handler) {
        handlers.add(handler);
    }


}

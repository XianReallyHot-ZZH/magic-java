package com.example.magic07patternchain.validation;

import com.example.magic07patternchain.exception.ValidatorException;

import java.util.ArrayList;
import java.util.List;

/**
 * 验证上下文,会在链上一直往下传递
 */
public class ValidatorContext {

    private List<String> errorMessageList = new ArrayList<>();

    public void addErrorMessage(String message) {
        errorMessageList.add(message);
    }

    public void throwExceptionIfHasError() throws ValidatorException {
        if (errorMessageList.size() > 0) {
            throw new ValidatorException(errorMessageList.toString());
        }
    }

}

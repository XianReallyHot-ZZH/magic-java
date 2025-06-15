package com.example.magic07patternchain.validation;

import com.example.magic07patternchain.exception.ValidatorException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 验证上下文,会在链上一直往下传递
 */
public class ValidatorContext {

    private List<String> errorMessageList = new ArrayList<>();

    private boolean stop = false;

    private int index = 0;

    private Object value;

    private Map<String, Object> data = new HashMap<>();

    public ValidatorContext(Object value) {
        this.value = value;
    }

    public void addErrorMessage(String message) {
        errorMessageList.add(message);
    }

    public void doNext(Object value) {
        index++;
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    public int currentIndex() {
        return index;
    }

    public void stopChain() {
        stop = true;
    }

    public boolean shouldStop() {
        return stop;
    }

    public void put(String key, Object value) {
        data.put(key, value);
    }

    public Object get(String key) {
        return data.get(key);
    }

    public void throwExceptionIfHasError() throws ValidatorException {
        if (errorMessageList.size() > 0) {
            throw new ValidatorException(errorMessageList.toString());
        }
    }

}

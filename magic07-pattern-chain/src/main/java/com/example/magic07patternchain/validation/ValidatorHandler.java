package com.example.magic07patternchain.validation;

import com.example.magic07patternchain.exception.ValidatorException;

public interface ValidatorHandler {

    void validate(Object value) throws ValidatorException;

}

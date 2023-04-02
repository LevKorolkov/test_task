package com.levkorolkov.test_application.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ApplicationNotFoundException extends RuntimeException {
    public ApplicationNotFoundException(String msg) {
        super(msg);
    }
}

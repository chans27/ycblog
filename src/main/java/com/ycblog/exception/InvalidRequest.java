package com.ycblog.exception;

import lombok.Getter;

/**
 * status -> 400
 */
@Getter

public class InvalidRequest extends YcblogException {

    private static final String MESSAGE = "Bad Request";

    public InvalidRequest() {
        super(MESSAGE);
    }

    public InvalidRequest(String fieldName, String message) {
        super(MESSAGE);
        addValidation(fieldName, message);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}

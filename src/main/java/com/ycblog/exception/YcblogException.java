package com.ycblog.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * high level Exception
 */
@Getter
public abstract class YcblogException extends RuntimeException {

    public final Map<String, String> validation = new HashMap<>();

    public YcblogException(String message) {
        super(message);
    }

    public YcblogException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract int getStatusCode();

    public void addValidation(String fieldName, String message) {
        validation.put(fieldName, message);
    }
}

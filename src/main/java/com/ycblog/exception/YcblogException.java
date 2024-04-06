package com.ycblog.exception;

public abstract class YcblogException extends RuntimeException {

    public YcblogException(String message) {
        super(message);
    }

    public YcblogException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract int getStatusCode();
}

package com.ycblog.exception;

/**
 * status -> 401
 */
public class Unauthorized extends YcblogException {

    private static final String MESSAGE = "authorization required.";

    public Unauthorized() {
        super(MESSAGE);
    }

    public Unauthorized(Throwable cause) {
        super(MESSAGE, cause);
    }

    @Override
    public int getStatusCode() {
        return 401;
    }
}

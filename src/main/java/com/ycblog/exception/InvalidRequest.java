package com.ycblog.exception;

/**
 * status -> 400
 */
public class InvalidRequest extends YcblogException {

    private static final String MESSAGE = "Bad Request";

    public InvalidRequest() {
        super(MESSAGE);
    }

    public InvalidRequest(Throwable cause) {
        super(MESSAGE, cause);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}

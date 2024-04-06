package com.ycblog.exception;

/**
 * status -> 404
 */
public class PostNotFound extends YcblogException {

    private static final String MESSAGE = "Post does not exist";

    public PostNotFound() {
        super(MESSAGE);
    }

    public PostNotFound(Throwable cause) {
        super(MESSAGE, cause);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}

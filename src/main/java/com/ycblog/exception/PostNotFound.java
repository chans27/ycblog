package com.ycblog.exception;

public class PostNotFound extends RuntimeException {

    private static final String MESSAGE = "Post does not exist";

    public PostNotFound() {
        super(MESSAGE);
    }

    public PostNotFound(Throwable cause) {
        super(MESSAGE, cause);
    }
}

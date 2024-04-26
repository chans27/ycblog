package com.ycblog.exception;

public class InvalidSIgnInInformation extends YcblogException{

    private static final String MESSAGE = "ID or Password is invalid.";

    public InvalidSIgnInInformation() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}

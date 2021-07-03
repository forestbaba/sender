package com.forestsoftware.send.exceptions;

public class InvalidRequestBodyException extends Exception{
    public InvalidRequestBodyException() {
        super();
    }

    public InvalidRequestBodyException(String message) {
        super(message);
    }

    public InvalidRequestBodyException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidRequestBodyException(Throwable cause) {
        super(cause);
    }

    protected InvalidRequestBodyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

package com.forestsoftware.send.exceptions;

import org.springframework.http.HttpStatus;

public class ErrorMessage {

    private HttpStatus httpStatus;
    private String message;

    public ErrorMessage() {
    }

    public ErrorMessage( String message,HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

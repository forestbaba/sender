package com.forestsoftware.send.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
@ResponseStatus
public class ResponseEntityException extends ResponseEntityExceptionHandler {

    Map<String, Object> response= new LinkedHashMap<>();

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<Object> usernameAlreadyExist(UserAlreadyExistException e, WebRequest webRequest){
        ErrorMessage errorMessage = new ErrorMessage(e.getMessage(), HttpStatus.UNAUTHORIZED);

        response.put("error", true);
        response.put("message", e.getMessage());
        response.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<Object>(response, HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(InvalidRequestBodyException.class)
    public ResponseEntity<Object>invalidRequestBody(InvalidRequestBodyException e, WebRequest webRequest){
        response.put("error", true);
        response.put("message", e.getMessage());
        response.put("timestamp", LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}

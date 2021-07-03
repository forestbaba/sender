package com.forestsoftware.send.services;

import com.forestsoftware.send.exceptions.InvalidRequestBodyException;
import com.forestsoftware.send.exceptions.UserAlreadyExistException;
import com.forestsoftware.send.model.User;
import com.forestsoftware.send.request.ForgotPasswordRequest;
import com.forestsoftware.send.request.LoginRequest;
import com.forestsoftware.send.request.SignupRequest;
import com.forestsoftware.send.request.UpdatePasswordRequest;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface UserService {
    User createUser(SignupRequest user1) throws UserAlreadyExistException, InvalidRequestBodyException;

    ResponseEntity<?> login(LoginRequest loginRequest);

    ResponseEntity<?> updatePassword(UpdatePasswordRequest updatePasswordRequest);

    ResponseEntity<?> forgotPassword(ForgotPasswordRequest forgotPasswordRequest) throws InvalidRequestBodyException;

    ResponseEntity<?> setPassword(Map<String, Object> forgotPasswordRequest) throws InvalidRequestBodyException;
}

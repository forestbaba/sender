package com.forestsoftware.send.controller;


import com.forestsoftware.send.exceptions.InvalidRequestBodyException;
import com.forestsoftware.send.exceptions.UserAlreadyExistException;
import com.forestsoftware.send.model.User;
import com.forestsoftware.send.repository.RoleRepository;
import com.forestsoftware.send.repository.UserRepository;
import com.forestsoftware.send.request.ForgotPasswordRequest;
import com.forestsoftware.send.request.LoginRequest;
import com.forestsoftware.send.request.SignupRequest;
import com.forestsoftware.send.response.MessageResponse;
import com.forestsoftware.send.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/users/auth")
public class UserAuthController {



    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @RequestMapping(method =RequestMethod.GET, value = "/check", produces = "application/json")
    @ResponseBody
    public ResponseEntity checkAuth(){

        return new ResponseEntity<String>("Hello, you!", HttpStatus.OK);    }

    @PostMapping("/signup")
    public ResponseEntity<Object> signupUp(@Valid @RequestBody SignupRequest user) throws UserAlreadyExistException, InvalidRequestBodyException {

      User newUser =   userService.createUser(user);
        return MessageResponse.generateResponse("Registration success",HttpStatus.CREATED,newUser);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) throws InvalidRequestBodyException {
       return userService.login(loginRequest);
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<?> ForgotPassword(@RequestBody ForgotPasswordRequest forgotPasswordRequest) throws InvalidRequestBodyException {
        return userService.forgotPassword(forgotPasswordRequest);
    }

    @PostMapping("/setPassword")
    public ResponseEntity<?>setPassword(@RequestBody Map<String, Object> forgotPasswordRequest) throws InvalidRequestBodyException {
        return userService.setPassword(forgotPasswordRequest);
    }
}

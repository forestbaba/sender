package com.forestsoftware.send.controller;

import com.forestsoftware.send.exceptions.InvalidRequestBodyException;
import com.forestsoftware.send.request.UpdatePasswordRequest;
import com.forestsoftware.send.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/users")
public class Usercontroller {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/updatePassword")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> updatePassword(@Valid @RequestBody UpdatePasswordRequest updatePasswordRequest) throws InvalidRequestBodyException {
        return userService.updatePassword(updatePasswordRequest);
    }
}

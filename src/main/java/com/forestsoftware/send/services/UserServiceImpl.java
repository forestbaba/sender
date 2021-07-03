package com.forestsoftware.send.services;

import com.forestsoftware.send.exceptions.InvalidRequestBodyException;
import com.forestsoftware.send.exceptions.UserAlreadyExistException;
import com.forestsoftware.send.model.ERole;
import com.forestsoftware.send.model.Role;
import com.forestsoftware.send.model.User;
import com.forestsoftware.send.repository.RoleRepository;
import com.forestsoftware.send.repository.UserRepository;
import com.forestsoftware.send.request.*;
import com.forestsoftware.send.security.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    Map<String, Object> response = new HashMap<>();

    @Override
    public User createUser(SignupRequest user) throws UserAlreadyExistException, InvalidRequestBodyException {

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistException("Email already in use");
        }

        if (userRepository.existsByPhone(user.getPhone())) {
            throw new UserAlreadyExistException("Phone number already in use");
        }

        User user1 = new User(user.getPhone(), user.getEmail(), passwordEncoder.encode(user.getPassword()));
        // return userService.createUser(user);

        if (user.getEmail().isEmpty() || user.getPhone().isEmpty() || user.getPassword().isEmpty() || user.getPassword() == null) {
            throw new InvalidRequestBodyException("All fields are required:[name, email, phone]");
        }

        Set<String> sRoles = user.getRole();
        Set<Role> roles = new HashSet<>();

        if (sRoles == null) {
            Role role = roleRepository.findRoleByName(ERole.ROLE_USER).orElseThrow(() -> new RuntimeException("Error: Role not found"));
            roles.add(role);
        } else {
            sRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findRoleByName(ERole.ROLE_ADMIN).orElseThrow(() -> new RuntimeException("Error: Role is not found"));
                        roles.add(adminRole);
                        break;

                    case "mod":
                        Role modRole = roleRepository.findRoleByName(ERole.ROLE_MODERATOR).orElseThrow(() -> new RuntimeException("Error: Role is not found"));
                        roles.add(modRole);
                        break;


                    default:
                        Role userRole = roleRepository.findRoleByName(ERole.ROLE_USER).orElseThrow(() -> new RuntimeException("Error: Role is not found"));
                        roles.add(userRole);
                        break;
                }
            });
        }
        user1.setRoles(roles);
        userRepository.save(user1);
        return user1;
    }

    @Override
    public ResponseEntity<?> login(LoginRequest loginRequest) throws InvalidRequestBodyException {
        boolean check1 = userRepository.existsByEmail(loginRequest.getEmail());
        boolean check2 = userRepository.existsByPhone(loginRequest.getPhone());

        if(check1){
          User user =  userRepository.findUserByEmail(loginRequest.getEmail());
          if(!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())){
              throw new InvalidRequestBodyException("Invalid credentials, try again later");
          }
        }else if(check2){
          User user =  userRepository.findUserByPhone(loginRequest.getPhone());
          if(!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())){
              throw new InvalidRequestBodyException("Invalid credentials, try again later");
          }
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));


        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenUtil.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    @Override
    public ResponseEntity<?> updatePassword(UpdatePasswordRequest updatePasswordRequest) throws InvalidRequestBodyException {

        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        User user = userRepository.findUserById(userDetails.getId());
        System.out.println("The old password: "+updatePasswordRequest.getOldPassword());
        if(passwordEncoder.matches( updatePasswordRequest.getOldPassword(), user.getPassword())){
            String newPassword = passwordEncoder.encode(updatePasswordRequest.getNewPassword());
            user.setPassword(newPassword);
            userRepository.save(user);
            response.put("error", true);
            response.put("message", "Password updated Successfully");
            response.put("user",user);

            return  ResponseEntity.ok(response);
        }else {
            throw new InvalidRequestBodyException("oldPassword do not match");
        }
    }

    @Override
    public ResponseEntity<Map<String, Object>> forgotPassword(ForgotPasswordRequest forgotPasswordRequest) throws InvalidRequestBodyException {

        Map<String, Object> map = new HashMap<>();
        logger.info("Send email or token to user email or phone number");

        Optional userWithEmail = userRepository.findByEmail(forgotPasswordRequest.getEmail());
        Optional userWithPhone = userRepository.findByPhone(forgotPasswordRequest.getPhone());

        //TODO: This is not validating all the fields, check it out
        if (forgotPasswordRequest.getPhone().trim().isEmpty()) {
            System.out.println("Yes Go ahead");
            if (forgotPasswordRequest.getEmail().trim().isEmpty()) {
                throw new InvalidRequestBodyException("Email or phone is required");
            }
        }

        if (!userWithEmail.isPresent() && userWithEmail.isPresent()) {

            throw new UsernameNotFoundException(String.format("User with %s and %s not found", userWithEmail, userWithPhone));
        } else {
            if (userWithEmail.isPresent()) {
                //TODO: Send email with code with actual Email
                int min = 10000;
                int max = 1000000;
                int a = (int) (Math.random() * (max - min + 1) + min);
                User user = userRepository.findUserByEmail(forgotPasswordRequest.getEmail());
                user.setVerificationCode(a);
                user.setVerificationCodeTime(LocalDateTime.now());
                userRepository.save(user);

                map.put("error", false);
                map.put("message", "A verification has been sent to your phone and email, Please check to update your password");
                return new ResponseEntity<Map<String, Object>>(map, HttpStatus.CREATED);

            }
        }
        return null;
    }

    @Override
    public ResponseEntity<?> setPassword(Map<String, Object> forgotPasswordRequest) throws InvalidRequestBodyException {
        int code =0;
        String newPassword = passwordEncoder.encode(forgotPasswordRequest.get("newPassword").toString());

        if (forgotPasswordRequest.get("email") == null && forgotPasswordRequest.get("phone") == null) {
            throw new InvalidRequestBodyException("Phone or email is required");
        }
        if (forgotPasswordRequest.get("code") == null || forgotPasswordRequest.get("code").toString().trim() == "") {
            throw new InvalidRequestBodyException("code is either invalid or incorrect");
        }
        if (forgotPasswordRequest.get("newPassword") == null || forgotPasswordRequest.get("newPassword").toString().trim() == "") {
            throw new InvalidRequestBodyException("newPassword is required");
        }

        if (forgotPasswordRequest.get("confirmPassword") == null || forgotPasswordRequest.get("confirmPassword").toString().trim() == "") {
            throw new InvalidRequestBodyException("confirmPassword is required");
        }

        if (!forgotPasswordRequest.get("newPassword").equals(forgotPasswordRequest.get("confirmPassword"))) {
            throw new InvalidRequestBodyException("confirmPassword and newPassword do not match");
        }

        User user1 = userRepository.findUserByEmail(forgotPasswordRequest.get("email").toString());
        User user2 = userRepository.findUserByPhone(forgotPasswordRequest.get("phone").toString());


        if (userRepository.existsByEmail(forgotPasswordRequest.get("email").toString())) {

             code = user1.getVerificationCode();

            if (code == Integer.parseInt( forgotPasswordRequest.get("code").toString())) {
                long seconds = Duration.between(LocalTime.now(), user1.getVerificationCodeTime()).getSeconds() / 60;
                if(seconds > 30){
                    response.put("error", true);
                    response.put("message", "code has expired");
                    return  ResponseEntity.badRequest().body(response);
                }

                user1.setPassword(newPassword);
                userRepository.save(user1);
                response.put("error", false);
                response.put("message", "password updated successfully");
                response.put("user", user1);
                return ResponseEntity.ok(response);
            }else {
                response.put("message", "Code do not match");
                response.put("error", true);

                return ResponseEntity.badRequest().body(response);
            }

        } else if (userRepository.existsByPhone(forgotPasswordRequest.get("phone").toString())) {

             code = user2.getVerificationCode();
            long seconds = Duration.between(LocalTime.now(), user2.getVerificationCodeTime()).getSeconds() / 60;

            logger.error(">>>>>>**" + seconds % 30 );

            if (code == Integer.parseInt( forgotPasswordRequest.get("code").toString())) {
                if(seconds % 30 > 0){
                    response.put("error", true);
                    response.put("message", "code has expired");
                    return  ResponseEntity.badRequest().body(response);
                }
                user2.setPassword(newPassword);
                userRepository.save(user2);
                response.put("error", false);
                response.put("message", "password updated successfully");
                response.put("user", user2);
                return ResponseEntity.ok(response);
            }else {
                response.put("message", "Code do not match");
                response.put("error", true);

                return ResponseEntity.badRequest().body(response);
            }
        } else {
            throw new UsernameNotFoundException("User does not exist");
        }

    }

    private boolean createPassword(Map<String, Object> forgotPasswordRequest, Map<String, Object> response, User user1) {
        int code = user1.getVerificationCode();
        if (code == (Integer) forgotPasswordRequest.get("code")) {
            String newPassword = passwordEncoder.encode(forgotPasswordRequest.get("password").toString());
            userRepository.save(user1);
            response.put("error", false);
            response.put("message", "password updated successfully");
            response.put("user", user1);
            return true;
        }
        return false;
    }
}

package com.forestsoftware.send.request;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LoginRequest {
    private String email;
    private String password;
    private String phone;

    public LoginRequest(String email, String password){
        this.email = email;
        this.password = password;
    }
    public String getEmail() {
        return email;
    }
    public String getPassword(){
        return password;
    }
}

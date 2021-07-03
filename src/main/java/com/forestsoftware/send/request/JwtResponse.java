package com.forestsoftware.send.request;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class JwtResponse implements Serializable {
    private static final long serialVersionUID = -8091879091924046844L;
    private final String jwttoken;
    private  int  id;
    private String phone;
    private String email;
    private List<String>roles;


    public JwtResponse(String jwttoken, Integer id, String phone, String email, List<String> roles) {
        this.jwttoken = jwttoken;
        this.id = id;
        this.phone = phone;
        this.email = email;
        this.roles = roles;
    }

    public String getToken() {
        return this.jwttoken;
    }
}

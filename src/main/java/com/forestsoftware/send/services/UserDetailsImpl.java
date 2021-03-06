package com.forestsoftware.send.services;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.forestsoftware.send.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserDetailsImpl implements UserDetails {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String email;
    private String phone;

    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;



    public UserDetailsImpl(Integer id, String email, String phone, String password, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.authorities = authorities;
    }

    public static UserDetailsImpl build(User user){
        List<GrantedAuthority>authorities = user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());

        return new UserDetailsImpl(
                user.getId(),
                user.getEmail(),
                user.getPhone(),
                user.getPassword(),
                authorities
        );
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
    public Integer getId(){
        return id;
    }
    public String getEmail(){
        return email;
    }

    public String getPassword(){
        return password;

    }

    @Override
    public String getUsername() {
        return phone;
    }

    public String getPhone(){
        return phone;
    }

    public boolean isAccountNonExpired(){
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }


}

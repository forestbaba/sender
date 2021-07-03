package com.forestsoftware.send.model;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users", uniqueConstraints={
@UniqueConstraint(columnNames = "email"),
        @UniqueConstraint(columnNames = "phone")
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotNull(message="firstname can not be empty")
    private String firstname;

    @NotNull(message = "lastname is required")
    private String lastname;

    @UniqueElements
    @NotNull(message = "phone is required")
    private String phone;

    @NotNull(message = "email is required")
    @UniqueElements
    private String email;

    @NotNull(message = "password is required")
    private String password;

    private int verificationCode;
    private LocalDateTime verificationCodeTime;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role>roles = new HashSet<>();

    @Column(columnDefinition = "boolean default false")
    @NotNull
    private Boolean verified;
    private Date verifiedAt;


    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createdAt;

    @Column(nullable = false)
    @UpdateTimestamp
    private Date updatedAt;

    public User() {
    }

    public User(@UniqueElements @NotNull(message = "phone is required") String phone, @NotNull(message = "email is required") @UniqueElements String email, @NotNull(message = "password is required") String password) {
        this.phone = phone;
        this.email = email;
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }



    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public Date getVefifiedAt() {
        return verifiedAt;
    }

    public void setVefifiedAt(Date vefifiedAt) {
        this.verifiedAt = vefifiedAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public int getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(int verificationCode) {
        this.verificationCode = verificationCode;
    }

    public LocalDateTime getVerificationCodeTime() {
        return verificationCodeTime;
    }

    public void setVerificationCodeTime(LocalDateTime verificationCodeTime) {
        this.verificationCodeTime = verificationCodeTime;
    }
}

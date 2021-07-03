package com.forestsoftware.send.request;

import javax.validation.constraints.AssertTrue;

public class ForgotPasswordRequest {

    private String email;
    private String phone;
    private String newPassword;
    private String confirmPassword;
    private int code;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @AssertTrue(message = "username or email is required")
    public boolean isPhoneOrEmailExists() {
        return this.getPhone() != null || this.getEmail() != null;
    }
}

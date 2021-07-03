package com.forestsoftware.send.request;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class UpdatePasswordRequest {
    @NotBlank
    @NotNull(message = "Old password is required")
    private String oldPassword;

    @NotBlank
    @NotNull(message = "New password is required")
    private String newPassword;

    public String getOldPassword() {
        return oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }
}

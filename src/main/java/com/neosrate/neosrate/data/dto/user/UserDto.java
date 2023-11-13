package com.neosrate.neosrate.data.dto.user;

import jakarta.validation.constraints.NotBlank;

public class UserDto {
    @NotBlank(message = "Username is required")
//    @Size(min = 3, max = 30, message = "Username must be between 3 and 30 characters")
    private String username;

    @NotBlank(message = "Email is required")
//    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    private String passwd;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }
}

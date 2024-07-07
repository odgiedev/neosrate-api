package com.neosrate.neosrate.data.dto.user;

import com.neosrate.neosrate.data.enums.UserRole;
import jakarta.validation.constraints.*;

public class UserDto {
    @NotBlank(message = "Username is required.")
    @Pattern(regexp = "^(?=[a-zA-Z0-9]{3,25}$)[a-zA-Z0-9]+$", message = "Username must be between 3 and 25 characters (alphanumeric).")
    private String username;

    @NotBlank(message = "Email is required.")
    @Email(message = "Invalid email format.")
    private String email;

    @NotBlank(message = "Password is required.")
    @Size(min = 6, max = 80, message = "Password must be between 6 and 80 characters.")
    private String passwd;

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    private UserRole role;

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

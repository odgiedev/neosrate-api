package com.neosrate.neosrate.data.dto.user;

import jakarta.validation.constraints.NotBlank;

public class UserProfileDto {
    @NotBlank(message = "Bio is required")
    private String bio;
}

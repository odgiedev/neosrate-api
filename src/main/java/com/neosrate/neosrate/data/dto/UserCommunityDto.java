package com.neosrate.neosrate.data.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UserCommunityDto {
    private Integer userId;

    @NotBlank(message = "*username* cannot be null.")
    private String username;

    @NotBlank(message = "*community* cannot be null.")
    private String community;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCommunity() {
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
    }
}

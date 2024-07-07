package com.neosrate.neosrate.data.dto.user;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserProfileDto {
    private Integer userId;

    private String username;

    private String pfpName;

    private String bio;

    private String communityOwner;

    private String communityParticipant;

    private String createdAt;

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getPfpName() {
        return pfpName;
    }

    public void setPfpName(String pfpName) {
        this.pfpName = pfpName;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getCommunityOwner() {
        return communityOwner;
    }

    public void setCommunityOwner(String communityOwner) {
        this.communityOwner = communityOwner;
    }

    public String getCommunityParticipant() {
        return communityParticipant;
    }

    public void setCommunityParticipant(String communityParticipant) {
        this.communityParticipant = communityParticipant;
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
}

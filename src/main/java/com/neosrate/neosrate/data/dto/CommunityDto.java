package com.neosrate.neosrate.data.dto;

import jakarta.validation.constraints.NotBlank;

public class CommunityDto {
    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    @NotBlank(message = "*creator* is required")
    private String creator;

    private String totalParticipant;

    private String totalPost;

    private String createdAt;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getTotalParticipant() {
        return totalParticipant;
    }

    public void setTotalParticipant(String totalParticipant) {
        this.totalParticipant = totalParticipant;
    }

    public String getTotalPost() {
        return totalPost;
    }

    public void setTotalPost(String totalPost) {
        this.totalPost = totalPost;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}

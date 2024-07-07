package com.neosrate.neosrate.data.dto.Community;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class CommunityUpdateDto {
    private Integer communityId;

//    @NotBlank(message = "Name is required")
//    @Pattern(regexp = "^[A-Za-z]+$", message = "Only letters, not space.")
    private String name;

    private String description;

    private String pfp;

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

    public String getPfp() {
        return pfp;
    }

    public void setPfp(String pfp) {
        this.pfp = pfp;
    }

    public Integer getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Integer communityId) {
        this.communityId = communityId;
    }
}

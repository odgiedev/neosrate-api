package com.neosrate.neosrate.data.dto.user;

public class UserProfileDto {
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPfpPath() {
        return pfpPath;
    }

    public void setPfpPath(String pfpPath) {
        this.pfpPath = pfpPath;
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

    private String userId;

    private String pfpPath;

    private String bio;

    private String communityOwner;

    private String communityParticipant;
}

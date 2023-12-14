package com.neosrate.neosrate.data.model;

import jakarta.persistence.*;

@Entity
public class UserProfile {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "pfp_path")
    private String pfpPath;

    private String bio = "bio";

    @Column(name = "community_owner")
    private String communityOwner;

    @Column(name = "community_participant")
    private String communityParticipant;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
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
}

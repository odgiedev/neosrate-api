package com.neosrate.neosrate.data.model;

import jakarta.persistence.*;

@Entity
public class UserProfile {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(nullable = false)
    private String username;

    private String bio = "bio";

    @Column(name = "created_at", nullable = false)
    private String createdAt;

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

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

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

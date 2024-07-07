package com.neosrate.neosrate.data.model;

import jakarta.persistence.*;

@Entity
public class Post {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(nullable = false)
    private String community;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String title;

    private String text;

    @Column(name = "created_at", nullable = false)
    private String createdAt;

    @Column(name = "dislike_count", columnDefinition = "INT DEFAULT 0", nullable = false)
    private Integer dislikeCount;

    @Column(name = "like_count", columnDefinition = "INT DEFAULT 0", nullable = false)
    private Integer likeCount;

    @Column(name = "file_path")
    private String filePath;

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

    public String getCommunity() {
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getDislikeCount() {
        return dislikeCount;
    }

    public void setDislikeCount(Integer dislikeCount) {
        this.dislikeCount = dislikeCount;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}

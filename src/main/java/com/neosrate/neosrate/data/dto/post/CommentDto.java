package com.neosrate.neosrate.data.dto.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CommentDto {
    private Integer id;

    @NotNull(message = "*userId* is required.")
    private Integer userId;

    @NotNull(message = "*postId* is required.")
    private Integer postId;

    @NotBlank(message = "*community* is required.")
    private String community;

    @NotBlank(message = "*creator* is required.")
    private String creator;

    @NotBlank(message = "Comment is required.")
    private String comment;

    private String createdAt;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCommunity() {
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}

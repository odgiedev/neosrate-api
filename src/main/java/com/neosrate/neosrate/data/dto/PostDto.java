package com.neosrate.neosrate.data.dto;
import jakarta.validation.constraints.NotBlank;

public class PostDto {
    @NotBlank(message = "*user_id* cannot be null.")
    private Integer userId;

    @NotBlank(message = "*username* cannot be null.")
    private String username;

    @NotBlank(message = "Title cannot be null.")
    private String title;

    @NotBlank(message = "Text cannot be null.")
    private String text;

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
}

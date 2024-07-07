package com.neosrate.neosrate.data.dto.post;

public class PostDto {
    //@NotBlank(message = "*user_id* cannot be null.")
    private Integer userId;

    //@NotBlank(message = "Community cannot be null.")
    private String community;

    //@NotBlank(message = "*username* cannot be null.")
    private String username;

    //@NotBlank(message = "Title cannot be null.")
    private String title;

    //@NotBlank(message = "Text cannot be null.")
    private String text;

    private String communityPic;

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

    public String getCommunityPic() {
        return communityPic;
    }

    public void setCommunityPic(String communityPic) {
        this.communityPic = communityPic;
    }
}

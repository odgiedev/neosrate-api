package com.neosrate.neosrate.data.dto.post;

public class PostUpdateDto {
    private String title;
    private String text;
    private String filePath;
    private String communityPic;
    private Integer likeCount;
    private Integer dislikeCount;
    private Integer likeTrigger;
    private Integer userIdLiker;

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

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public Integer getDislikeCount() {
        return dislikeCount;
    }

    public void setDislikeCount(Integer dislikeCount) {
        this.dislikeCount = dislikeCount;
    }

    public Integer getLikeTrigger() {
        return likeTrigger;
    }

    public void setLikeTrigger(Integer likeTrigger) {
        this.likeTrigger = likeTrigger;
    }

    public Integer getUserIdLiker() {
        return userIdLiker;
    }

    public void setUserIdLiker(Integer userIdLiker) {
        this.userIdLiker = userIdLiker;
    }

    public String getCommunityPic() {
        return communityPic;
    }

    public void setCommunityPic(String communityPic) {
        this.communityPic = communityPic;
    }
}

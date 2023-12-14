package com.neosrate.neosrate.data;

import com.neosrate.neosrate.data.model.Post;
import com.neosrate.neosrate.data.model.UserLike;

import java.util.List;

public class PostAndLikeResponse {
    private List<Post> posts;
    private List<UserLike> userLikes;

    public PostAndLikeResponse(List<Post> posts, List<UserLike> userLikes) {
        this.posts = posts;
        this.userLikes = userLikes;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public List<UserLike> getUserLikes() {
        return userLikes;
    }
}

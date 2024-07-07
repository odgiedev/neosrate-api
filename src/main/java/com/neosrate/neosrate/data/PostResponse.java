package com.neosrate.neosrate.data;

import com.neosrate.neosrate.data.model.Comment;
import com.neosrate.neosrate.data.model.Post;
import com.neosrate.neosrate.data.model.UserLike;

import java.util.List;

public class PostResponse {
    private Iterable<Post> posts;
    private Iterable<UserLike> userLikes;

    private Iterable<Comment> comments;

    private Long totalPosts;

    public PostResponse(Iterable<Post> posts, Iterable<UserLike> userLikes, Iterable<Comment> comments, Long totalPosts) {
        this.posts = posts;
        this.userLikes = userLikes;
        this.comments = comments;
        this.totalPosts = totalPosts;
    }

    public Iterable<Post> getPosts() {
        return posts;
    }

    public Iterable<UserLike> getUserLikes() {
        return userLikes;
    }

    public Iterable<Comment> getComments() {
        return comments;
    }

    public Long getTotalPosts() {
        return totalPosts;
    }
}

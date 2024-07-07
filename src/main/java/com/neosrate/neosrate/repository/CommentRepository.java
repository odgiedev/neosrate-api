package com.neosrate.neosrate.repository;

import com.neosrate.neosrate.data.model.Comment;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CommentRepository extends CrudRepository<Comment, Integer> {
    List<Comment> findByCommunity(String community);

    List<Comment> findByCreator(String username);

    void deleteAllByCommunity(String communityName);

    void deleteAllByUserId(Integer ownerId);

    void deleteByPostId(Integer postId);
}

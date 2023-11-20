package com.neosrate.neosrate.repository;

import com.neosrate.neosrate.data.model.Post;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PostRepository extends CrudRepository<Post, Integer> {
    List<Post> findByUserId(Integer userId);

    List<Post> findByCommunity(String community);
}

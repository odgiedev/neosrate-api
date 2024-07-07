package com.neosrate.neosrate.repository;

import  com.neosrate.neosrate.data.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PostRepository extends CrudRepository<Post, Integer> {
    List<Post> findByUsername(String username, PageRequest id);

    List<Post> findByCommunity(String community, PageRequest id);

    List<Post> findByTitleContains(String username, PageRequest id);

    Page<Post> findAll(Pageable pageable);

    long countByTitleContains(String searchQuery);

    long countByCommunity(String community);

    long countByUsername(String username);

    List<Post> findAllByCommunity(String string);

    void deleteAllByCommunity(String communityName);

    void deleteAllByUserId(Integer ownerId);
}

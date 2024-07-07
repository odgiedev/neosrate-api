package com.neosrate.neosrate.repository;

import com.neosrate.neosrate.data.model.Community;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CommunityRepository extends CrudRepository<Community, Integer> {
    Community findByName(String community);

    List<Community> findByCreator(String username);

    void deleteAllByCreator(String username);
}

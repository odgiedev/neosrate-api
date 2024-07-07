package com.neosrate.neosrate.repository;

import com.neosrate.neosrate.data.model.UserCommunity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserCommunityRepository extends CrudRepository<UserCommunity, Integer> {
    UserCommunity findByUserIdAndCommunity(Integer userId, String community);

    List<UserCommunity> findByCommunity(String community);

    List<UserCommunity> findByUserId(Integer userId);

    void deleteByUserIdAndCommunity(Integer userId, String community);

    void deleteAllByCommunity(String communityName);

    void deleteAllByUserId(Integer ownerId);
}

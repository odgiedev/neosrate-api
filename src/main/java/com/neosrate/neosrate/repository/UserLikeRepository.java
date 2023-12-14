package com.neosrate.neosrate.repository;

import com.neosrate.neosrate.data.model.UserLike;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserLikeRepository extends CrudRepository<UserLike, Integer> {
    UserLike findByPostIdAndUserId(Integer postId, Integer userId);

    List<UserLike> findByUserId(Integer userId);

    List<UserLike> findByCommunity(String community);
}

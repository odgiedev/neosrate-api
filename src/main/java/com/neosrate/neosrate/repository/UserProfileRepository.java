package com.neosrate.neosrate.repository;

import com.neosrate.neosrate.data.model.UserProfile;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserProfileRepository extends CrudRepository<UserProfile, Integer> {
    Optional<UserProfile> findByUserId(Integer userId);
}

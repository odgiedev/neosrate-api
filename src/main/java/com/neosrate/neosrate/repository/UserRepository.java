package com.neosrate.neosrate.repository;

import com.neosrate.neosrate.data.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    UserDetails findByEmail(String email);
}

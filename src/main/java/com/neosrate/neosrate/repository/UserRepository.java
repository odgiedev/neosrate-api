package com.neosrate.neosrate.repository;

import com.neosrate.neosrate.data.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    UserDetails findByEmail(String email);

    List<User> findByEmailOrUsername(String email, String username);

    User findByUsername(String username);

    Page<User> findAll(Pageable pageable);
}

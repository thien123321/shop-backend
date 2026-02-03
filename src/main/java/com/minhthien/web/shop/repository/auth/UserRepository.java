package com.minhthien.web.shop.repository.auth;

import com.minhthien.web.shop.entity.Auth.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUsername(String username);
    User findByUsername(String username);
    User findUserByUsername(String username);

    Optional<User> findByEmail(String email);


}

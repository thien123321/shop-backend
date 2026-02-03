package com.minhthien.web.shop.repository.auth;

import com.minhthien.web.shop.entity.Auth.PasswordResetOtp;
import com.minhthien.web.shop.entity.Auth.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordResetOtpRepository extends JpaRepository<PasswordResetOtp, Long> {
    Optional<PasswordResetOtp> findByUser(User user);
    void deleteByUser(User user);
}

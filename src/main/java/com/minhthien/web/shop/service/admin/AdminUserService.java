package com.minhthien.web.shop.service.admin;

import com.minhthien.web.shop.dto.user.UserResponse;
import com.minhthien.web.shop.entity.Auth.User;
import com.minhthien.web.shop.enums.Role;
import com.minhthien.web.shop.enums.UserStatus;
import com.minhthien.web.shop.repository.auth.PasswordResetOtpRepository;
import com.minhthien.web.shop.repository.auth.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetOtpRepository passwordResetOtpRepository;

    /* GET /api/admin/users */
    public Page<UserResponse> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return userRepository.findAll(pageable).map(this::mapToResponse);
    }

    /* GET /api/admin/users/{id} */
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return mapToResponse(user);
    }

    private UserResponse mapToResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .phone(user.getPhone())
                .address(user.getAddress())
                .avatar(user.getAvatar())
                .role(user.getRole())
                .enabled(true)
                .build();
    }


    /* LOCK user */
    public void lockUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() == Role.ADMIN) {
            throw new RuntimeException("Cannot lock ADMIN user");
        }

        user.setLocked(true);
        userRepository.save(user);
    }

    /* UNLOCK user */
    public void unlockUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setLocked(false);
        userRepository.save(user);
    }

    /* UPDATE STATUS: ACTIVE / INACTIVE */
    public void updateStatus(Long id, UserStatus status) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setStatus(status);
        userRepository.save(user);
    }
}

package com.minhthien.web.shop.service.staff;

import com.minhthien.web.shop.dto.user.UserRequest;
import com.minhthien.web.shop.dto.user.UserResponse;
import com.minhthien.web.shop.entity.Auth.User;
import com.minhthien.web.shop.enums.Role;
import com.minhthien.web.shop.repository.auth.PasswordResetOtpRepository;
import com.minhthien.web.shop.repository.auth.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StaffService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetOtpRepository passwordResetOtpRepository;


    public UserResponse createUser(UserRequest request) {
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .address(request.getAddress())
                .age(request.getAge())
                .gender(request.getGender())
                .avatar(request.getAvatar())
                .role(request.getRole())
                .enabled(request.getEnabled())
                .build();
        return mapToResponse(userRepository.save(user));
    }


    public UserResponse updateUser(Long id, UserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setEmail(request.getEmail());
        user.setRole(request.getRole());
        user.setEnabled(request.getEnabled());

        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        return mapToResponse(userRepository.save(user));
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

        if(user.getRole() == Role.ADMIN) {
            throw new RuntimeException("Admin user cannot be deleted");
        }

        passwordResetOtpRepository.deleteByUser(user);
        userRepository.deleteById(id);
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
}

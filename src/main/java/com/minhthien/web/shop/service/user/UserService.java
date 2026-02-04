package com.minhthien.web.shop.service.user;

import com.minhthien.web.shop.dto.user.ChangePasswordRequest;
import com.minhthien.web.shop.dto.user.UpdateProfileRequest;
import com.minhthien.web.shop.dto.user.UserResponse;
import com.minhthien.web.shop.entity.Auth.User;
import com.minhthien.web.shop.repository.auth.UserRepository;
import com.minhthien.web.shop.service.upload.ImageStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ImageStorageService imageStorageService;


    // ===== COMMON =====
    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("Unauthenticated");
        }

        String username = auth.getName();

        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        return user;
    }



    private UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullname(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .address(user.getAddress())
                .avatar(user.getAvatar())
                .build();
    }

    // ===== GET ME =====
    public UserResponse getMe() {
        return toResponse(getCurrentUser());
    }

    // ===== UPDATE PROFILE =====
    public UserResponse updateProfile(UpdateProfileRequest req) {
        User user = getCurrentUser();
        user.setName(req.getName());
        user.setEmail(req.getEmail());
        user.setPhone(req.getPhone());
        user.setAddress(req.getAddress());
        return toResponse(userRepository.save(user));
    }

    // ===== CHANGE PASSWORD =====
    public void changePassword(ChangePasswordRequest req) {
        User user = getCurrentUser();

        if (!passwordEncoder.matches(req.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("Old password incorrect");
        }

        if (req.getNewPassword().length() < 6) {
            throw new RuntimeException("Password too short");
        }

        user.setPassword(passwordEncoder.encode(req.getNewPassword()));
        userRepository.save(user);
    }

    // ===== UPDATE AVATAR =====
    public UserResponse updateAvatar(MultipartFile file) {
        User user = getCurrentUser();

        // (optional) xoá avatar cũ
        if (user.getAvatar() != null && !user.getAvatar().isEmpty()) {
            String oldFile = user.getAvatar().replace("/upload/", "");
            imageStorageService.deleteFile(oldFile);
        }

        // upload ảnh mới
        String storedFileName = imageStorageService.storeFile(file);

        // build url public
        String avatarUrl = "/upload/" + storedFileName;

        user.setAvatar(avatarUrl);
        userRepository.save(user);

        return toResponse(user);
    }

}
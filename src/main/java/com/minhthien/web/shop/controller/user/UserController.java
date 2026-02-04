    package com.minhthien.web.shop.controller.user;

    import com.minhthien.web.shop.dto.user.ChangePasswordRequest;
    import com.minhthien.web.shop.dto.user.UpdateProfileRequest;
    import com.minhthien.web.shop.dto.user.UserResponse;
    import com.minhthien.web.shop.service.user.UserService;
    import lombok.RequiredArgsConstructor;
    import org.springframework.http.MediaType;
    import org.springframework.http.ResponseEntity;
    import org.springframework.security.access.prepost.PreAuthorize;
    import org.springframework.web.bind.annotation.*;
    import org.springframework.web.multipart.MultipartFile;

    @RestController
    @RequiredArgsConstructor
    @RequestMapping("/api/user")
    @PreAuthorize("hasRole('USER')")

    public class UserController {

        private final UserService userService;


        @GetMapping("/me")
        public UserResponse me() {
            return userService.getMe();
        }


        @PutMapping("/me")
        public UserResponse updateProfile(
                @RequestBody UpdateProfileRequest req) {
            return userService.updateProfile(req);
        }

        @PutMapping("/me/password")
        public ResponseEntity<?> changePassword(
                @RequestBody ChangePasswordRequest req) {
            userService.changePassword(req);
            return ResponseEntity.ok("Password updated");
        }

        @PutMapping(value = "/me/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        public UserResponse updateAvatar(
                @RequestPart("file") MultipartFile file
        ) {
            return userService.updateAvatar(file);
        }

    }

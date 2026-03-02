package com.minhthien.web.shop.controller.auth;

import com.minhthien.web.shop.dto.auth.*;
import com.minhthien.web.shop.dto.auth.ResetPasswordRequest;
import com.minhthien.web.shop.entity.Auth.User;
import com.minhthien.web.shop.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request){
        RegisterResponse user = authService.register(request);

        return ResponseEntity.ok(
                new RegisterResponse(
                        user.getName(),
                        user.getEmail(),
                        user.getPhone(),
                        user.getAddress(),
                        user.getGender(),
                        user.getAge(),
                        user.getRole(),
                        user.getMessage()
                )
        );
    }


    @PostMapping("/login")

    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest){


        LoginResponse respon = authService.login(loginRequest);

        return ResponseEntity.ok(
                new LoginResponse(
                        respon.getToken(),
                        respon.getName(),
                        respon.getEmail(),
                        respon.getPhone(),
                        respon.getAddress(),
                        respon.getGender(),
                        respon.getAge(),
                        respon.getRole(),
                        respon.getMessage()
                )

        );

    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(
            @RequestBody ForgotPasswordRequest request
    ) {
        authService.forgotPassword(request.getEmail());
        return ResponseEntity.ok("OTP đã gửi vào email");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ForgotPasswordResponse> resetPassword(
            @RequestBody ResetPasswordRequest request
    ) {
        return ResponseEntity.ok(
                authService.resetPassword(request)
        );
    }


    @PostMapping("/logout")
    public ResponseEntity<?> logout(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        authService.logout(user);
        return ResponseEntity.ok("Logout successful");
    }
}

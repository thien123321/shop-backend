package com.minhthien.web.shop.service.auth;


import com.minhthien.web.shop.dto.auth.*;
import com.minhthien.web.shop.dto.auth.ResetPasswordRequest;
import com.minhthien.web.shop.entity.Auth.PasswordResetOtp;
import com.minhthien.web.shop.entity.Auth.User;
import com.minhthien.web.shop.enums.Role;
import com.minhthien.web.shop.repository.auth.PasswordResetOtpRepository;
import com.minhthien.web.shop.repository.auth.RefreshTokenRepository;
import com.minhthien.web.shop.repository.auth.UserRepository;
import com.minhthien.web.shop.service.security.TokenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final PasswordResetOtpRepository otpRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public RegisterResponse register(RegisterRequest registerRequest) {
        if(userRepository.existsByUsername(registerRequest.getUsername())) {
            throw  new RuntimeException("Username already exists");
        }

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        User user = User.builder()
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .name(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .age(registerRequest.getAge())
                .gender(registerRequest.getGender())
                .address(registerRequest.getAddress())
                .phone(registerRequest.getPhone())
                .role(Role.USER)
                .build();

        userRepository.save(user);

        return RegisterResponse.builder()
                .name(user.getName())
                .email(user.getEmail())
                .age(user.getAge())
                .gender(user.getGender())
                .address(user.getAddress())
                .phone(user.getPhone())

                .message("Registered Successfully")
                .build();

    }

    public LoginResponse login(LoginRequest loginRequest) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        User user = userRepository.findUserByUsername(loginRequest.getUsername());


        if(user == null) throw  new BadCredentialsException("Invalid username or password");

        String token = tokenService.generateToken(user);

        return LoginResponse.builder()
                .token(token)
                .name(user.getName())
                .email(user.getEmail())
                .age(user.getAge())
                .gender(user.getGender())
                .address(user.getAddress())
                .phone(user.getPhone())
                .message("Login Success")
                .build();

    }

    @Transactional

    public void forgotPassword(String email) {

        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Email không tồn tại"));

        otpRepository.deleteByUser(user);

        String otp = String.valueOf(
                new Random().nextInt(900000) + 100000
        );

        PasswordResetOtp resetOtp = new PasswordResetOtp();
        resetOtp.setOtp(otp);
        resetOtp.setUser(user);
        resetOtp.setExpiredAt(LocalDateTime.now().plusMinutes(5));

        otpRepository.save(resetOtp);

        emailService.sendOtpEmail(email, otp);
    }

    @Transactional
    // verify otp va forgot password
    public ForgotPasswordResponse resetPassword(ResetPasswordRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Email không tồn tại"));

        PasswordResetOtp resetOtp = otpRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("OTP không tồn tại"));

        if (!resetOtp.getOtp().equals(request.getOtp())) {
            throw new RuntimeException("OTP không đúng");
        }

        if (resetOtp.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP đã hết hạn");
        }

        user.setPassword(
                passwordEncoder.encode(request.getNewPassword())
        );
        userRepository.save(user);

        otpRepository.delete(resetOtp);

        String newToken = tokenService.generateToken(user);

        return new ForgotPasswordResponse(newToken);
    }

    public void logout(User user) {
        refreshTokenRepository.deleteByUser(user);

    }
}

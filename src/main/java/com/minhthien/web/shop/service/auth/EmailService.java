package com.minhthien.web.shop.service.auth;

public interface EmailService {
    void sendOtpEmail(String to, String otp);
}

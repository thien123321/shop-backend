package com.minhthien.web.shop.service.Auth;

public interface EmailService {
    void sendOtpEmail(String to, String otp);
}

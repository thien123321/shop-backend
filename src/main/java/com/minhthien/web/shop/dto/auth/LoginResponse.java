package com.minhthien.web.shop.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class LoginResponse {
    private String token;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String gender;
    private int age;
    private String message;
}

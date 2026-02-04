package com.minhthien.web.shop.dto.user;


import com.minhthien.web.shop.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {
    private String username;
    private String password;
    private String name;
    private String email;
    private String phone;
    private String address;
    private int age;
    private String gender;
    private String avatar;
    private Role role;
    private Boolean enabled;

}

package com.example.center_management.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateMeRequest {

    // Cho phép đổi "tên hiển thị" – hiện map vào username
    private String fullName;

    // Cho phép đổi mật khẩu
    private String password;
}

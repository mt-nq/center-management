package com.example.center_management.dto.request;

import lombok.Data;

@Data
public class AuthRegisterRequest {
    private String username;
    private String password;
    private String email;
    private String fullName;
}

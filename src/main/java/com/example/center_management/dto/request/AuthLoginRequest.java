package com.example.center_management.dto.request;

import lombok.Data;

@Data
public class AuthLoginRequest {
    private String username;
    private String password;
}

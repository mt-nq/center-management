package com.example.center_management.dto.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthLoginRequest {

    private String username;
    private String password;
}

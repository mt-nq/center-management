package com.example.center_management.controller;

import com.example.center_management.dto.auth.AdminCreateRequest;
import com.example.center_management.dto.auth.AuthLoginRequest;
import com.example.center_management.dto.auth.StudentRegisterRequest;
import com.example.center_management.dto.response.UserSimpleResponse;
import com.example.center_management.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // POST /auth/register (Public)
    @PostMapping("/auth/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserSimpleResponse register(@RequestBody StudentRegisterRequest request) {
        return authService.registerStudent(request);
    }

    // POST /auth/login (Public)
    @PostMapping("/auth/login")
    public UserSimpleResponse login(@RequestBody AuthLoginRequest request) {
        return authService.login(request);
    }

    // POST /admin/users/create-admin (Admin-only)
    @PostMapping("/admin/users/create-admin")
    @ResponseStatus(HttpStatus.CREATED)
    public UserSimpleResponse createAdmin(@RequestBody AdminCreateRequest request) {
        return authService.createAdmin(request);
    }
}

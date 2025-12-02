package com.example.center_management.service;

import com.example.center_management.dto.auth.AuthLoginRequest;
import com.example.center_management.dto.auth.StudentRegisterRequest;
import com.example.center_management.dto.response.AuthResponse;

public interface AuthService {

    AuthResponse registerStudent(StudentRegisterRequest request);

    AuthResponse login(AuthLoginRequest request);
}

package com.example.center_management.service;

import com.example.center_management.dto.auth.AdminCreateRequest;
import com.example.center_management.dto.auth.AuthLoginRequest;
import com.example.center_management.dto.auth.StudentRegisterRequest;
import com.example.center_management.dto.response.UserSimpleResponse;

public interface AuthService {

    UserSimpleResponse registerStudent(StudentRegisterRequest request);

    UserSimpleResponse login(AuthLoginRequest request);

    UserSimpleResponse createAdmin(AdminCreateRequest request);
}

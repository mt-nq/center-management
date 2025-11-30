package com.example.center_management.service;

import com.example.center_management.dto.auth.AuthLoginRequest;
import com.example.center_management.dto.auth.StudentRegisterRequest;
import com.example.center_management.dto.auth.AdminCreateRequest;
import com.example.center_management.dto.response.UserSimpleResponse;

public interface AuthService {

    // Đăng ký học viên
    UserSimpleResponse registerStudent(StudentRegisterRequest request);

    // Đăng nhập (admin + student)
    UserSimpleResponse login(AuthLoginRequest request);

    // Tạo tài khoản admin mới
    UserSimpleResponse createAdmin(AdminCreateRequest request);
}

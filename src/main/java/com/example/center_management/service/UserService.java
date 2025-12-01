package com.example.center_management.service;

import com.example.center_management.dto.auth.UserSimpleResponse;

public interface UserService {

    // Lấy thông tin user hiện tại (dành cho /api/users/me)
    UserSimpleResponse getCurrentUser();
}

package com.example.center_management.service;

import com.example.center_management.dto.request.UserCreateRequest;
import com.example.center_management.dto.response.UserResponse;

import java.util.List;

public interface UserService {

    UserResponse create(UserCreateRequest request);

    List<UserResponse> getAll();
}

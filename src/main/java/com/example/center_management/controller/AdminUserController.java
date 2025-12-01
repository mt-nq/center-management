package com.example.center_management.controller;

import java.util.List;

import com.example.center_management.dto.auth.AdminCreateRequest;
import com.example.center_management.dto.response.UserResponse;
import com.example.center_management.dto.response.UserSimpleResponse;
import com.example.center_management.service.AuthService;
import com.example.center_management.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final AuthService authService;
    private final UserService userService;

    // POST /admin/users/create-admin – Tạo Admin mới (201)
    @PostMapping("/create-admin")
    @ResponseStatus(HttpStatus.CREATED)
    public UserSimpleResponse createAdmin(@Valid @RequestBody AdminCreateRequest request) {
        return authService.createAdmin(request);
    }

    // GET /admin/users – Lấy danh sách tất cả user (200)
    @GetMapping
    public List<UserResponse> getAll() {
        return userService.getAll();
    }
}

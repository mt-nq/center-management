package com.example.center_management.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.center_management.dto.response.UserResponse;
import com.example.center_management.dto.user.UserUpdateMeRequest;
import com.example.center_management.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class MeController {

    private final UserService userService;

    // GET /api/users/me – Lấy thông tin hồ sơ của người đang đăng nhập (200)
    // Tạm thời dùng query param userId vì chưa có JWT
    @GetMapping("/me")
    public UserResponse getMe(@RequestParam Long userId) {
        return userService.getMe(userId);
    }

    // PUT /api/users/me – Cập nhật hồ sơ cá nhân (200)
    @PutMapping("/me")
    public UserResponse updateMe(
            @RequestParam Long userId,
            @Valid @RequestBody UserUpdateMeRequest request
    ) {
        return userService.updateMe(userId, request);
    }
}

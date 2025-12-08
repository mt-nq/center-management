package com.example.center_management.controller.admin;

import java.util.List;

import com.example.center_management.dto.response.UserResponse;
import com.example.center_management.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService userService;
    // GET /admin/users – Lấy danh sách tất cả user (200)
    @GetMapping
    public List<UserResponse> getAll() {
        return userService.getAll();
    }
}

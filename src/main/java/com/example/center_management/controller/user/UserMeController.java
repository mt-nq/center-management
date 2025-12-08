package com.example.center_management.controller.user;

import com.example.center_management.dto.request.UserCreateRequest;
import com.example.center_management.dto.response.UserResponse;
import com.example.center_management.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserMeController {

    private final UserService userService;

    @PostMapping
    public UserResponse create(@Valid @RequestBody UserCreateRequest request) {
        return userService.create(request);
    }
}

package com.example.center_management.controller.user;

import com.example.center_management.dto.request.StudentUpdateRequest;
import com.example.center_management.dto.response.StudentResponse;
import com.example.center_management.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentMeController {

    private final StudentService studentService;

    // Student xem thông tin của chính mình
    @GetMapping("/me")
    public StudentResponse getMyProfile(@RequestParam Long studentId) {
        return studentService.getById(studentId);
    }

    // Student tự cập nhật thông tin cá nhân
    @PutMapping("/me")
    public StudentResponse updateMyProfile(
            @RequestParam Long studentId,
            @Valid @RequestBody StudentUpdateRequest request
    ) {
        return studentService.update(studentId, request);
    }
}

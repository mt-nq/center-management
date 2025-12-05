package com.example.center_management.controller.user;

import com.example.center_management.dto.request.StudentUpdateRequest;
import com.example.center_management.dto.response.StudentResponse;
import com.example.center_management.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentMeController {

    private final StudentService studentService;

    // Lấy thông tin profile từ JWT
    @GetMapping("/me")
    public StudentResponse getMyProfile(Authentication authentication) {
        String username = authentication.getName();
        Long studentId = studentService.findStudentIdByUsername(username);
        return studentService.getById(studentId);
    }

    // Cập nhật profile từ JWT
    @PutMapping("/me")
    public StudentResponse updateMyProfile(
            Authentication authentication,
            @Valid @RequestBody StudentUpdateRequest request
    ) {
        String username = authentication.getName();
        Long studentId = studentService.findStudentIdByUsername(username);
        return studentService.update(studentId, request);
    }
}

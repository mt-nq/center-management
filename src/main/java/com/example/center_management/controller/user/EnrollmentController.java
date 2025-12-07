package com.example.center_management.controller.user;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

import com.example.center_management.dto.response.EnrollmentResponse;
import com.example.center_management.dto.request.EnrollmentCreateRequest;
import com.example.center_management.service.EnrollmentService;
import com.example.center_management.service.StudentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;
    private final StudentService studentService;

    // Nếu sau này cho student tự enroll thì dùng JWT ở đây:
    @PostMapping
    public EnrollmentResponse enroll(
            Authentication authentication,
            @Valid @RequestBody EnrollmentCreateRequest request
    ) {
        String username = authentication.getName();
        Long studentId = studentService.findStudentIdByUsername(username);
        request.setStudentId(studentId);

        return enrollmentService.enroll(request);
    }

    // NEW: Student xem danh sách enrollment của chính mình
    @GetMapping("/me")
    public List<EnrollmentResponse> getMyEnrollments(Authentication authentication) {
        String username = authentication.getName();
        Long studentId = studentService.findStudentIdByUsername(username);
        return enrollmentService.getByStudent(studentId);
    }
}

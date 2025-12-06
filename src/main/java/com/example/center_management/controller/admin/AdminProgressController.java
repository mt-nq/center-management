package com.example.center_management.controller.admin;

import java.util.List;

import com.example.center_management.dto.response.EnrollmentProgressResponse;
import com.example.center_management.service.ProgressService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AdminProgressController {

    private final ProgressService progressService;

    // GET /admin/enrollments/{enrollmentId}/progress  (Admin)
    @GetMapping("/admin/enrollments/{enrollmentId}/progress")
    public EnrollmentProgressResponse getProgressForAdmin(@PathVariable Long enrollmentId) {
        return progressService.getProgressForAdmin(enrollmentId);
    }

    @GetMapping("/admin/enrollments/progress")
    public List<EnrollmentProgressResponse> getAllProgressForAdmin() {
        return progressService.getAllEnrollmentProgressForAdmin();
    }
}

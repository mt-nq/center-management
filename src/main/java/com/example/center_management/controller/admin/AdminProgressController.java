package com.example.center_management.controller.admin;

import com.example.center_management.dto.response.EnrollmentProgressResponse;
import com.example.center_management.service.ProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminProgressController {
    private final ProgressService progressService;
    // GET /admin/enrollments/{enrollmentId}/progress  (Admin)
    @GetMapping("/enrollments/{enrollmentId}/progress")
    public EnrollmentProgressResponse getProgressForAdmin(@PathVariable Long enrollmentId) {
        return progressService.getProgressForAdmin(enrollmentId);
    }

    @GetMapping("/enrollments/progress")
    public List<EnrollmentProgressResponse> getAllProgressForAdmin() {
        return progressService.getAllEnrollmentProgressForAdmin();
    }

     // GET /admin/students/{studentId}/courses -> Xem tất cả khóa học + tiến độ của 1 học viên
    @GetMapping("/students/{studentId}/courses")
    public List<EnrollmentProgressResponse> getCoursesOfStudent(@PathVariable Long studentId) {
        return progressService.getAllProgressByStudent(studentId);
    }
}

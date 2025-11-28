package com.example.center_management.controller;

import com.example.center_management.dto.request.LessonCompleteRequest;
import com.example.center_management.dto.response.EnrollmentProgressResponse;
import com.example.center_management.dto.response.LessonProgressResponse;
import com.example.center_management.service.ProgressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProgressController {

    private final ProgressService progressService;

    // POST /lessons/{lessonId}/complete  (Student)
    @PostMapping("/lessons/{lessonId}/complete")
    public LessonProgressResponse completeLesson(
            @PathVariable Long lessonId,
            @Valid @RequestBody LessonCompleteRequest request
    ) {
        return progressService.completeLesson(lessonId, request.getEnrollmentId());
    }

    // GET /enrollments/{enrollmentId}/progress  (Student)
    @GetMapping("/enrollments/{enrollmentId}/progress")
    public EnrollmentProgressResponse getProgress(@PathVariable Long enrollmentId) {
        return progressService.getProgress(enrollmentId);
    }

    // GET /admin/enrollments/{enrollmentId}/progress  (Admin)
    @GetMapping("/admin/enrollments/{enrollmentId}/progress")
    public EnrollmentProgressResponse getProgressForAdmin(@PathVariable Long enrollmentId) {
        return progressService.getProgressForAdmin(enrollmentId);
    }
}

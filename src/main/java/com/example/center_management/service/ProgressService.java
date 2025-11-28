package com.example.center_management.service;

import com.example.center_management.dto.response.EnrollmentProgressResponse;
import com.example.center_management.dto.response.LessonProgressResponse;

public interface ProgressService {

    LessonProgressResponse completeLesson(Long lessonId, Long enrollmentId);

    EnrollmentProgressResponse getProgress(Long enrollmentId);

    EnrollmentProgressResponse getProgressForAdmin(Long enrollmentId);
}

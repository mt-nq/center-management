package com.example.center_management.service;

import com.example.center_management.dto.response.EnrollmentProgressResponse;
import com.example.center_management.dto.response.LessonProgressResponse;
import com.example.center_management.dto.request.LessonCompleteRequest;

import java.util.List;

public interface ProgressService {

    LessonProgressResponse completeLesson(Long lessonId, Long enrollmentId);

    EnrollmentProgressResponse getProgress(Long enrollmentId);

    EnrollmentProgressResponse getProgressForAdmin(Long enrollmentId);

    List<EnrollmentProgressResponse> getEnrollmentsWithFullProgress();

    List<EnrollmentProgressResponse> getAllEnrollmentProgressForAdmin();

    List<EnrollmentProgressResponse> getAllProgressByStudent(Long studentId);
}


package com.example.center_management.dto.response;

import com.example.center_management.domain.enums.CompletionResult;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class EnrollmentProgressResponse {

    private Long enrollmentId;
    private Long studentId;
    private Long courseId;
    private String studentName;

    private String courseTitle;
    private long completedVideoLessons;
    private long totalVideoLessons;
    private double progressPercent;  // 0 – 100

    private boolean eligibleForCertificate;
    private CompletionResult completionResult;
}

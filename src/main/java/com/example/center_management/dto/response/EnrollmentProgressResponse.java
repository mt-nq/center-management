package com.example.center_management.dto.response;

import java.time.LocalDateTime;

import com.example.center_management.domain.enums.CompletionResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentProgressResponse {

    private Long enrollmentId;

    private Long studentId;
    private String studentCode;
    private String studentName;

    private Long courseId;
    private String courseCode;
    private String courseTitle;

    private double progressPercentage;

    private long totalVideoLessons;

    private long completedVideoLessons;

    private CompletionResult completionResult;

    private boolean eligibleForCertificate;

    private LocalDateTime enrolledAt;
}

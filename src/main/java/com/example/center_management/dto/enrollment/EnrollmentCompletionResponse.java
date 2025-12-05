package com.example.center_management.dto.enrollment;

import java.time.LocalDateTime;

import com.example.center_management.domain.enums.CompletionResult;
import com.example.center_management.domain.enums.EnrollmentStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnrollmentCompletionResponse {

    private Long enrollmentId;

    private Long courseId;
    private String courseTitle;

    private Long studentId;
    private String studentName;

    private EnrollmentStatus status;
    private CompletionResult result;

    private LocalDateTime completedAt;

    private boolean certificateIssued;
    private String certificateCode;
    private LocalDateTime certificateIssuedAt;
}

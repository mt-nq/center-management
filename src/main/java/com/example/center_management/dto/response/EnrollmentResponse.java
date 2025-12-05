package com.example.center_management.dto.response;

import java.time.LocalDateTime;

import com.example.center_management.domain.enums.CertificateResult;
import com.example.center_management.domain.enums.EnrollmentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnrollmentResponse {

    private Long id;

    // Student info
    private Long studentId;
    private String studentCode;
    private String studentName;

    // Course info
    private Long courseId;
    private String courseCode;
    private String courseTitle;

    // Enrollment info
    private EnrollmentStatus status;
    private Double progressPercentage;
    private LocalDateTime enrolledAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Certificate info (nếu có)
    private CertificateResult result;   // PASSED / FAILED hoặc null nếu chưa cấp
    private String certificateCode;     // mã chứng chỉ, hoặc null nếu chưa có
}

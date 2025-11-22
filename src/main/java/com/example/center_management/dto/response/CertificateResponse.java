package com.example.center_management.dto.response;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CertificateResponse {

    private Long enrollmentId;

    private Long studentId;
    private String studentCode;
    private String studentName;

    private Long courseId;
    private String courseCode;
    // Đổi lại tùy field trong Course, ví dụ: name / title / content
    private String courseName;

    private String result;          // PASSED / FAILED
    private String certificateCode; // null nếu FAILED
    private LocalDateTime issuedAt;
}

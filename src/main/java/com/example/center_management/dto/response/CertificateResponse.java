package com.example.center_management.dto.response;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class CertificateResponse {
    private Long id;
    private Long enrollmentId;

    private Long studentId;
    private String studentCode;
    private String studentName;

    private Long courseId;
    private String courseCode;
    private String courseName;

    private String result; // PASSED / FAILED
    private String certificateCode;
    private LocalDate issuedAt;
    private String status; // Valid / Revoked

    private String notes;
}

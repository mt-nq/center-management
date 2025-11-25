package com.example.center_management.dto.certificate;

import java.time.LocalDateTime;

import com.example.center_management.domain.enums.CertificateResult;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CertificateResponse {

    private Long id;
    private Long enrollmentId;
    private Long studentId;
    private Long courseId;

    private String certificateNo;
    private LocalDateTime issuedAt;
    private CertificateResult result; // PASS / FAIL
}

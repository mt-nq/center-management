package com.example.center_management.dto.response;

import java.time.LocalDateTime;

import com.example.center_management.domain.enums.CertificateResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor   // <-- để dùng new CertificateResponse(...)
@NoArgsConstructor
public class CertificateResponse {

    private Long id;
    private Long enrollmentId;
    private Long studentId;
    private Long courseId;

    private String certificateCode;
    private LocalDateTime issuedAt;
    private CertificateResult result;
}

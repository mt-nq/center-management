// CertificateResponse.java
package com.example.center_management.dto.response;

import com.example.center_management.domain.enums.CertificateStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class CertificateResponse {

    private String certificateCode;
    private Integer enrollmentId;
    private Integer courseId;
    private String courseName;
    private Integer studentId;
    private String studentUsername;
    private LocalDate issuedDate;
    private CertificateStatus status;
}

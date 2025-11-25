package com.example.center_management.dto.request;

import lombok.Data;

@Data
public class CertificateCreateRequest {
    private Long enrollmentId;
    private String status; // Valid / Revoked
    private String notes;
}

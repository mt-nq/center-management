package com.example.center_management.dto.certificate;

import com.example.center_management.domain.enums.CertificateResult;

import lombok.Data;

@Data
public class IssueCertificateRequest {

    // PASS hoặc FAIL
    private CertificateResult result;
}

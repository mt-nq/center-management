package com.example.center_management.service;

import com.example.center_management.domain.enums.CertificateResult;
import com.example.center_management.dto.certificate.CertificateResponse;

public interface CertificateService {

    CertificateResponse issueCertificate(Long enrollmentId, CertificateResult result);
}

package com.example.center_management.controller;

import org.springframework.web.bind.annotation.*;

import com.example.center_management.domain.enums.CertificateResult;
import com.example.center_management.dto.certificate.CertificateResponse;
import com.example.center_management.dto.certificate.IssueCertificateRequest;
import com.example.center_management.service.CertificateService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/certificates")
@RequiredArgsConstructor
public class CertificateController {

    private final CertificateService certificateService;

    // Cấp chứng chỉ cho 1 enrollment (đã COMPLETED)
    @PostMapping("/enrollment/{enrollmentId}")
    public CertificateResponse issueCertificate(
            @PathVariable Long enrollmentId,
            @RequestBody IssueCertificateRequest request
    ) {
        CertificateResult result = request.getResult();
        return certificateService.issueCertificate(enrollmentId, result);
    }

    
}

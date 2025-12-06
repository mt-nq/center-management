package com.example.center_management.controller.admin;

import org.springframework.web.bind.annotation.*;

import com.example.center_management.domain.enums.CertificateResult;
import com.example.center_management.dto.certificate.IssueCertificateRequest;
import com.example.center_management.service.CertificateService;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;

import com.example.center_management.dto.response.CertificateResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/certificates")
@RequiredArgsConstructor
public class AdminCertificateController {

private final CertificateService certificateService;


@PostMapping("/enrollment/{enrollmentId}")
public CertificateResponse issueCertificate(
        @PathVariable Long enrollmentId,
        @RequestBody IssueCertificateRequest request
) {
CertificateResult result = request.getResult();
return certificateService.issueCertificate(enrollmentId, result);
}
@GetMapping("/enrollments")
public Page<CertificateResponse> getIssuedCertificates(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
) {
return certificateService.getAllIssuedCertificates(page, size);
}
@GetMapping("/history")
@PreAuthorize("hasRole('ADMIN')")
public Page<CertificateResponse> getCertificateHistory(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(required = false) String keyword,
        @RequestParam(required = false) CertificateResult result
) {
return certificateService.getCertificateHistory(page, size, keyword, result);
}
}

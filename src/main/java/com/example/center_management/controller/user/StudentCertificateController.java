package com.example.center_management.controller.user;

import com.example.center_management.dto.response.CertificateResponse;
import com.example.center_management.service.CertificateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
public class StudentCertificateController {

    private final CertificateService certificateService;

    // GET /api/student/{studentId}/certificates
    @GetMapping("/{studentId}/certificates")
    public List<CertificateResponse> getStudentCertificates(@PathVariable Long studentId) {
        return certificateService.getCertificatesOfStudent(studentId);
    }

    // GET /api/student/{studentId}/certificates/{enrollmentId}
    @GetMapping("/{studentId}/certificates/{enrollmentId}")
    public CertificateResponse getCertificateDetail(
            @PathVariable Long studentId,
            @PathVariable Long enrollmentId
    ) {
        return certificateService.getCertificateDetailOfStudent(studentId, enrollmentId);
    }
}

package com.example.center_management.controller.user;

import com.example.center_management.dto.response.CertificateResponse;
import com.example.center_management.service.CertificateService;
import com.example.center_management.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
public class StudentCertificateController {

    private final CertificateService certificateService;
    private final StudentService studentService;

    // GET /api/student/me/certificates
    @GetMapping("/me/certificates")
    public List<CertificateResponse> getMyCertificates(Authentication authentication) {
        String username = authentication.getName();
        Long studentId = studentService.findStudentIdByUsername(username);
        return certificateService.getCertificatesOfStudent(studentId);
    }

    // GET /api/student/me/certificates/{enrollmentId}
    @GetMapping("/me/certificates/{enrollmentId}")
    public CertificateResponse getMyCertificateDetail(
            Authentication authentication,
            @PathVariable Long enrollmentId
    ) {
        String username = authentication.getName();
        Long studentId = studentService.findStudentIdByUsername(username);
        return certificateService.getCertificateDetailOfStudent(studentId, enrollmentId);
    }
}

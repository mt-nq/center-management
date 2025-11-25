package com.example.center_management.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.center_management.domain.entity.Certificate;
import com.example.center_management.domain.entity.Enrollment;
import com.example.center_management.dto.request.CertificateCreateRequest;
import com.example.center_management.dto.response.CertificateResponse;
import com.example.center_management.exception.ResourceNotFoundException;
import com.example.center_management.repository.CertificateRepository;
import com.example.center_management.repository.EnrollmentRepository;
import com.example.center_management.service.CertificateService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CertificateServiceImpl implements CertificateService {

    private final CertificateRepository certificateRepository;
    private final EnrollmentRepository enrollmentRepository;

    @Override
    @Transactional
    public CertificateResponse create(CertificateCreateRequest request) {
        Enrollment enrollment = enrollmentRepository.findById(request.getEnrollmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found"));

        Certificate certificate = Certificate.builder()
                .enrollment(enrollment)
                .certificateCode(generateCertificateCode(enrollment.getId()))
                .issuedDate(LocalDate.now())
                .status(request.getStatus() != null ? request.getStatus() : "Valid")
                .build();

        Certificate saved = certificateRepository.save(certificate);
        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CertificateResponse> getAll() {
        return certificateRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CertificateResponse getById(Long id) {
        Certificate certificate = certificateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Certificate not found"));
        return toResponse(certificate);
    }

    @Override
    @Transactional
    public CertificateResponse revoke(Long id) {
        Certificate certificate = certificateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Certificate not found"));

        certificate.setStatus("Revoked");
        return toResponse(certificateRepository.save(certificate));
    }

    private CertificateResponse toResponse(Certificate certificate) {
        CertificateResponse res = new CertificateResponse();
        res.setId(certificate.getId());

        Enrollment enrollment = certificate.getEnrollment();
        if (enrollment != null) {
            res.setEnrollmentId(enrollment.getId());

            if (enrollment.getStudent() != null) {
                res.setStudentId(enrollment.getStudent().getId());
                res.setStudentCode(enrollment.getStudent().getCode()); // SỬA LẠI
                res.setStudentName(enrollment.getStudent().getFullName());
            }

            if (enrollment.getCourse() != null) {
                res.setCourseId(enrollment.getCourse().getId());
                res.setCourseCode(enrollment.getCourse().getCode()); // SỬA LẠI
                res.setCourseName(enrollment.getCourse().getTitle());
            }

            res.setResult(enrollment.getResult() != null ? enrollment.getResult() :
                    "Valid".equalsIgnoreCase(certificate.getStatus()) ? "PASSED" : "FAILED");
        }

        res.setCertificateCode(certificate.getCertificateCode());
        res.setIssuedAt(certificate.getIssuedDate());
        res.setStatus(certificate.getStatus());

        return res;
    }

    private String generateCertificateCode(Long enrollmentId) {
        return "CERT-" + LocalDate.now().getYear() + "-" + enrollmentId;
    }
}

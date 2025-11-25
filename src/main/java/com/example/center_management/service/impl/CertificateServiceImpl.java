package com.example.center_management.service.impl;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.center_management.domain.entity.Certificate;
import com.example.center_management.domain.entity.Enrollment;
import com.example.center_management.domain.enums.CertificateResult;
import com.example.center_management.domain.enums.EnrollmentStatus;
import com.example.center_management.dto.certificate.CertificateResponse;
import com.example.center_management.repository.CertificateRepository;
import com.example.center_management.repository.EnrollmentRepository;
import com.example.center_management.service.CertificateService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CertificateServiceImpl implements CertificateService {

    private final EnrollmentRepository enrollmentRepository;
    private final CertificateRepository certificateRepository;

    @Override
    public CertificateResponse issueCertificate(Long enrollmentId, CertificateResult result) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new NoSuchElementException("Enrollment not found"));

        if (enrollment.getStatus() != EnrollmentStatus.COMPLETED) {
            throw new IllegalStateException("Học viên chưa hoàn thành khóa học, không thể cấp chứng chỉ");
        }

        if (enrollment.getCertificate() != null) {
            throw new IllegalStateException("Enrollment này đã có chứng chỉ rồi");
        }

        String certificateNo = generateCertificateNo(enrollment);

        Certificate certificate = Certificate.builder()
                .enrollment(enrollment)
                .certificateNo(certificateNo)
                .issuedAt(LocalDateTime.now())
                .result(result)
                .build();

        Certificate saved = certificateRepository.save(certificate);
        enrollment.setCertificate(saved); // cập nhật relationship 2 chiều nếu cần

        return new CertificateResponse(
                saved.getId(),
                enrollment.getId(),
                enrollment.getStudent().getId(),
                enrollment.getCourse().getId(),
                saved.getCertificateNo(),
                saved.getIssuedAt(),
                saved.getResult()
        );
    }

    private String generateCertificateNo(Enrollment enrollment) {
        // Ví dụ: CERT-<courseId>-<enrollmentId>-<4 ký tự random>
        String random = UUID.randomUUID().toString().substring(0, 4).toUpperCase();
        return "CERT-" + enrollment.getCourse().getId() + "-" + enrollment.getId() + "-" + random;
    }
}

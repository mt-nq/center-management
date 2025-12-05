package com.example.center_management.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.center_management.domain.entity.Certificate;
import com.example.center_management.domain.entity.Course;
import com.example.center_management.domain.entity.Enrollment;
import com.example.center_management.domain.entity.Student;
import com.example.center_management.domain.enums.CertificateResult;
import com.example.center_management.domain.enums.EnrollmentStatus;
import com.example.center_management.dto.response.CertificateResponse;
import com.example.center_management.exception.ResourceNotFoundException;
import com.example.center_management.repository.CertificateRepository;
import com.example.center_management.repository.EnrollmentRepository;
import com.example.center_management.service.CertificateService;

import java.util.List;


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

        // Chỉ cho cấp chứng chỉ khi đã COMPLETED
        if (enrollment.getStatus() != EnrollmentStatus.COMPLETED) {
            throw new IllegalStateException("Học viên chưa hoàn thành khóa học, không thể cấp chứng chỉ");
        }

        // Không cho cấp trùng
        if (enrollment.getCertificate() != null) {
            throw new IllegalStateException("Enrollment này đã có chứng chỉ rồi");
        }

        // Tạo mã chứng chỉ
        String generatedCode = generateCertificateCode(enrollment);

        Certificate certificate = Certificate.builder()
                .enrollment(enrollment)
                .certificateCode(generatedCode)
                .issuedAt(LocalDateTime.now())
                .result(result)
                .build();

        Certificate saved = certificateRepository.save(certificate);

        return toResponse(saved);
    }

    private String generateCertificateCode(Enrollment enrollment) {
        // VD: CER-2025-00001
        return "CER-" + String.format("%05d", enrollment.getId());
    }


    @Override
    public Page<CertificateResponse> getAllIssuedCertificates(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Certificate> certPage = certificateRepository.findAll(pageable);
        return certPage.map(this::toResponse);
    }

    private CertificateResponse toResponse(Certificate certificate) {
        Enrollment enrollment = certificate.getEnrollment();
        Student student = (enrollment != null) ? enrollment.getStudent() : null;
        Course course = (enrollment != null) ? enrollment.getCourse() : null;

        Long enrollmentId = (enrollment != null) ? enrollment.getId() : null;
        Long studentId = (student != null) ? student.getId() : null;
        Long courseId = (course != null) ? course.getId() : null;

        return new CertificateResponse(
                certificate.getId(),
                enrollmentId,
                studentId,
                courseId,
                certificate.getCertificateCode(),
                certificate.getIssuedAt(),
                certificate.getResult()
        );
    }

    @Override
    public List<CertificateResponse> getCertificatesOfStudent(Long studentId) {
        return certificateRepository.findByEnrollment_Student_Id(studentId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public CertificateResponse getCertificateDetailOfStudent(Long studentId, Long enrollmentId) {
        Certificate cert = certificateRepository
                .findByEnrollment_IdAndEnrollment_Student_Id(enrollmentId, studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Certificate not found"));

        return toResponse(cert);
    }

}

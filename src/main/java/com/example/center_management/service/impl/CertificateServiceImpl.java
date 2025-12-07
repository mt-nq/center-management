package com.example.center_management.service.impl;

import java.time.LocalDateTime;
import java.util.List;
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
import com.example.center_management.domain.enums.CompletionResult;
import com.example.center_management.domain.enums.EnrollmentStatus;
import com.example.center_management.dto.response.CertificateResponse;
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
    

    // =============== CẤP CHỨNG CHỈ THỦ CÔNG (ISSUE TRỰC TIẾP) ===============
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

        String generatedCode = generateCertificateCode(enrollment);

        Certificate certificate = Certificate.builder()
                .enrollment(enrollment)
                .certificateCode(generatedCode)
                .issuedAt(LocalDateTime.now())
                .result(result) // PASS / FAIL
                .build();

        Certificate saved = certificateRepository.save(certificate);

        return toResponse(saved);
    }

    // =============== ĐỒNG BỘ TỪ ENROLLMENT → CERTIFICATE ===============
    @Override
    @Transactional
    public void syncFromEnrollment(Enrollment enrollment) {
        if (enrollment == null) return;

        // Lấy certificate (nếu đã tồn tại) ngay từ đầu
        Certificate certificate = certificateRepository
                .findByEnrollment(enrollment)
                .orElse(null);

        // 1) Nếu CHƯA COMPLETED → đảm bảo không có certificate hợp lệ
        if (enrollment.getStatus() != EnrollmentStatus.COMPLETED) {
            if (certificate != null) {
                certificate.setResult(CertificateResult.NOT_REVIEWED);
                certificate.setCertificateCode(null);
                certificate.setIssuedAt(null);
                certificateRepository.save(certificate);
            }
            return;
        }

        // 2) ĐÃ COMPLETED nhưng CHƯA có completionResult → không tạo cert, nếu có thì vô hiệu
        CompletionResult completionResult = enrollment.getCompletionResult();
        if (completionResult == null) {
            if (certificate != null) {
                certificate.setResult(CertificateResult.NOT_REVIEWED);
                certificate.setCertificateCode(null);
                certificate.setIssuedAt(null);
                certificateRepository.save(certificate);
            }
            return;
        }

        // 3) ĐÃ COMPLETED + CÓ completionResult → xử lý PASSED / FAILED
        switch (completionResult) {
            case NOT_REVIEWED -> handleNotReviewed(enrollment, certificate);
            case PASSED -> handlePassed(enrollment, certificate);
            case FAILED -> handleFailed(enrollment, certificate);
        }
    }

    private void handlePassed(Enrollment enrollment, Certificate certificate) {
        if (certificate == null) {
            certificate = new Certificate();
            certificate.setEnrollment(enrollment);
            certificate.setCertificateCode(generateCertificateCode(enrollment));
            certificate.setIssuedAt(LocalDateTime.now());
        }

        certificate.setResult(CertificateResult.PASS);

        if (certificate.getIssuedAt() == null) {
            certificate.setIssuedAt(LocalDateTime.now());
        }

        certificateRepository.save(certificate);
    }

    private void handleFailed(Enrollment enrollment, Certificate certificate) {
        if (certificate == null) {
            return;
        }

        certificate.setResult(CertificateResult.FAIL);
        certificate.setCertificateCode(null);
        certificate.setIssuedAt(null);

        certificateRepository.save(certificate);
    }

    private void handleNotReviewed(Enrollment enrollment, Certificate certificate) {
    if (certificate == null) {
        certificate = new Certificate();
        certificate.setEnrollment(enrollment);
    }

    // Chưa duyệt -> KHÔNG cấp code & issuedAt
    certificate.setResult(CertificateResult.NOT_REVIEWED);
    certificate.setCertificateCode(null);
    certificate.setIssuedAt(null);

    certificateRepository.save(certificate);
    }


    private String generateCertificateCode(Enrollment enrollment) {
        if (enrollment == null || enrollment.getId() == null) {
            return "CER-TEMP";
        }
        return "CER-" + String.format("%05d", enrollment.getId());
    }

    // =============== LẤY DANH SÁCH CHỨNG CHỈ ===============
    @Override
    public Page<CertificateResponse> getAllIssuedCertificates(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Certificate> certPage = certificateRepository.findAll(pageable);
        return certPage.map(this::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CertificateResponse> getCertificateHistory(
            int page,
            int size,
            String keyword,
            CertificateResult result
    ) {
        var pageable = PageRequest.of(page, size);

        String pattern = null;
        if (keyword != null && !keyword.isBlank()) {
            pattern = "%" + keyword.trim() + "%";
        }

        Page<Certificate> certPage = certificateRepository
                .searchCertificateHistory(
                        pattern,
                        result,
                        pageable
                );

        return certPage.map(this::toResponse);
    }


    private CertificateResponse toResponse(Certificate certificate) {
    if (certificate == null) {
        return null;
    }

    Enrollment enrollment = certificate.getEnrollment();
    Student student = (enrollment != null) ? enrollment.getStudent() : null;
    Course  course  = (enrollment != null) ? enrollment.getCourse()  : null;

    CertificateResponse resp = new CertificateResponse();

    if (enrollment != null) {
        resp.setEnrollmentId(enrollment.getId());
    }

    if (student != null) {
        resp.setStudentId(student.getId());
        resp.setStudentCode(student.getCode());
        resp.setStudentName(student.getFullName());
    }

    if (course != null) {
        resp.setCourseId(course.getId());
        resp.setCourseCode(course.getCode());
        resp.setCourseTitle(course.getTitle());

    }

    if (certificate.getResult() != null) {
        resp.setResult(certificate.getResult().name());
    } else {
        resp.setResult(null);
    }

    resp.setCertificateCode(certificate.getCertificateCode());
    resp.setIssuedAt(certificate.getIssuedAt());

    return resp;
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
                .orElseThrow(() -> new NoSuchElementException("Certificate not found"));

        return toResponse(cert);
    }
}

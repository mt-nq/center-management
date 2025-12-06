package com.example.center_management.service;

import com.example.center_management.domain.entity.Enrollment;
import com.example.center_management.domain.enums.CertificateResult;
import com.example.center_management.dto.response.CertificateResponse;
import org.springframework.data.domain.Page;
import java.util.List;

public interface CertificateService {

    CertificateResponse issueCertificate(Long enrollmentId, CertificateResult result);

    Page<CertificateResponse> getAllIssuedCertificates(int page, int size);


    // 👉 Dùng cho student: xem danh sách chứng chỉ của mình
    List<CertificateResponse> getCertificatesOfStudent(Long studentId);

    // 👉 Dùng cho student: xem chi tiết 1 chứng chỉ thuộc enrollment
    CertificateResponse getCertificateDetailOfStudent(Long studentId, Long enrollmentId);

    void syncFromEnrollment(Enrollment enrollment);
    
    Page<CertificateResponse> getCertificateHistory(
            int page,
            int size,
            String keyword,
            CertificateResult result
    );
}

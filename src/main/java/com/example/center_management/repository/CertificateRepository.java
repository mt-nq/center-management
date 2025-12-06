package com.example.center_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.center_management.domain.entity.Certificate;
import com.example.center_management.domain.entity.Enrollment;
import com.example.center_management.domain.enums.CertificateResult;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface CertificateRepository extends JpaRepository<Certificate, Long> {

    Page<Certificate> findAll(Pageable pageable);

    boolean existsByEnrollmentId(Long enrollmentId);

    List<Certificate> findByEnrollment_Student_Id(Long studentId);

    Optional<Certificate> findByEnrollment(Enrollment enrollment);

    Optional<Certificate> findByEnrollment_IdAndEnrollment_Student_Id(Long enrollmentId, Long studentId);

    Page<Certificate> findAllByOrderByIssuedAtDesc(Pageable pageable);

    // Tìm theo keyword (tên HV / tên khóa học / mã chứng chỉ) + optional result
        @Query("""
        SELECT c FROM Certificate c
        JOIN c.enrollment e
        JOIN e.student s
        JOIN e.course  co
        WHERE 
            (:pattern IS NULL OR 
                s.fullName ILIKE :pattern
                OR co.title ILIKE :pattern
                OR c.certificateCode ILIKE :pattern
        )
        AND (:result IS NULL OR c.result = :result)
        ORDER BY c.issuedAt DESC
        """)
    Page<Certificate> searchCertificateHistory(
            @Param("pattern") String pattern,
            @Param("result") CertificateResult result,
            Pageable pageable
    );
}

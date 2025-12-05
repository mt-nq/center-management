package com.example.center_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.center_management.domain.entity.Certificate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface CertificateRepository extends JpaRepository<Certificate, Long> {

    Page<Certificate> findAll(Pageable pageable);

    boolean existsByEnrollmentId(Long enrollmentId);

    List<Certificate> findByEnrollment_Student_Id(Long studentId);

    Optional<Certificate> findByEnrollment_IdAndEnrollment_Student_Id(Long enrollmentId, Long studentId);

}

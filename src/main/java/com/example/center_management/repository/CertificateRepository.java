package com.example.center_management.repository;

import com.example.center_management.domain.entity.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CertificateRepository extends JpaRepository<Certificate, Integer> {

    List<Certificate> findByEnrollment_Student_Id(Integer studentId);

    Optional<Certificate> findByCertificateCode(String certificateCode);
}

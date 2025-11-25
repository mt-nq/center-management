package com.example.center_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.center_management.domain.entity.Certificate;

public interface CertificateRepository extends JpaRepository<Certificate, Long> {
}

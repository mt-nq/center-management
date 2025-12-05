package com.example.center_management.domain.entity;

import java.time.LocalDateTime;

import com.example.center_management.domain.enums.CertificateResult;

import jakarta.persistence.*;
import lombok.*;
// com.example.center_management.domain.entity.Certificate

@Entity
@Table(name = "certificates")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Certificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // certificate luôn gắn với 1 enrollment
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enrollment_id", nullable = false)
    private Enrollment enrollment;

    @Enumerated(EnumType.STRING)
    @Column(name = "result", nullable = false)
    private CertificateResult result; // PASSED / FAILED

    @Column(name = "certificate_code", length = 50, unique = true)
    private String certificateCode;

    @Column(name = "issued_at")
    private LocalDateTime issuedAt;
}


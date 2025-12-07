package com.example.center_management.domain.entity;

import java.time.LocalDateTime;

import com.example.center_management.domain.enums.CertificateResult;

import jakarta.persistence.*;
import lombok.*;

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

    // Mỗi enrollment có tối đa 1 certificate
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enrollment_id", nullable = false, unique = true)
    private Enrollment enrollment;

    @Enumerated(EnumType.STRING)
    @Column(name = "result", nullable = false)
    private CertificateResult result; // NOT_REVIEWED / PASS / FAIL

    @Column(name = "certificate_code", length = 50, unique = true)
    private String certificateCode;

    @Column(name = "issued_at")
    private LocalDateTime issuedAt;
}

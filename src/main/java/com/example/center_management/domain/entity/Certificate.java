package com.example.center_management.domain.entity;

import com.example.center_management.domain.enums.CertificateStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

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
    private Integer id;

    // mỗi enrollment tối đa 1 certificate
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enrollment_id", nullable = false, unique = true)
    private Enrollment enrollment;

    @Column(name = "certificate_code", length = 100, unique = true)
    private String certificateCode;

    @Column(name = "issued_date")
    private LocalDate issuedDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private CertificateStatus status;
}

package com.example.center_management.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // username: unique
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 100)
    private String fullName;

    // ADMIN / STUDENT
    @Column(nullable = false, length = 20)
    private String role;

    // ACTIVE / INACTIVE (để khóa tài khoản)
    @Column(nullable = false, length = 20)
    private String status;

    @CreationTimestamp
    private LocalDateTime createdAt;
}

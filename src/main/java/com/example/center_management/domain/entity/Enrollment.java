package com.example.center_management.domain.entity;

import java.time.LocalDateTime;

import com.example.center_management.domain.enums.CompletionResult;
import com.example.center_management.domain.enums.EnrollmentStatus;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "enrollments")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Mỗi enrollment gắn với 1 học viên
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    // Mỗi enrollment gắn với 1 khóa học
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(name = "enrolled_at", nullable = false)
    private LocalDateTime enrolledAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private EnrollmentStatus status; // ENROLLED / COMPLETED / NOT_COMPLETED

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @OneToOne(mappedBy = "enrollment", fetch = FetchType.LAZY)
    private Certificate certificate;

    @Enumerated(EnumType.STRING)
    @Column(name = "completion_result")
    private CompletionResult completionResult = CompletionResult.NOT_REVIEWED;

    @Column(name = "completion_date")
    private LocalDateTime completionDate;
}

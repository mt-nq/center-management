package com.example.center_management.domain.entity;

import com.example.center_management.domain.enums.ResultStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "enrollments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // student_id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    // order_id unique, có thể null nếu admin tạo tay
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", unique = true)
    private Order order;

    @Column(name = "register_date")
    private LocalDate registerDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "result", length = 20)
    private ResultStatus result;

    @PrePersist
    public void prePersist() {
        if (registerDate == null) {
            registerDate = LocalDate.now();
        }
        if (result == null) {
            result = ResultStatus.DANG_HOC;
        }
    }
}

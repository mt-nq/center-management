package com.example.center_management.domain.entity;

import com.example.center_management.domain.enums.CourseStatus;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "lessons")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String title; // tên bài học

    @Column(length = 50)
    private String type; // VIDEO / QUIZ / DOCUMENT...

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chapter_id")
    private Chapter chapter;

    @Column(name = "url_vid")
    private String urlVid;

    @Enumerated(EnumType.STRING)
    private CourseStatus status;

}

package com.example.center_management.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "lessons")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id", nullable = false)
    private Section section;

    @Column(name = "lesson_title", length = 255, nullable = false)
    private String lessonTitle;

    // video / quiz / document
    @Column(name = "lesson_type", length = 50, nullable = false)
    private String lessonType;

    @Column(name = "video_url", length = 255)
    private String videoUrl;

    @Column(name = "duration_seconds")
    private Integer durationSeconds;
}

package com.example.center_management.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "chapters")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Chapter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(name = "chapter_name", length = 255, nullable = false)
    private String chapterName;

    // "order" là keyword nên map sang sortOrder, column thực vẫn là "order"
    @Column(name = "\"order\"", nullable = false)
    private Integer sortOrder;
}

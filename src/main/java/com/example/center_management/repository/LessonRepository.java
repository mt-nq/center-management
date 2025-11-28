package com.example.center_management.repository;

import com.example.center_management.domain.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonRepository extends JpaRepository<Lesson, Long> {

    // Đếm tổng số bài học VIDEO trong 1 khoá học
    long countByChapter_Course_IdAndType(Long courseId, String type);
}

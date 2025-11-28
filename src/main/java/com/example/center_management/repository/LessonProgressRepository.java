package com.example.center_management.repository;

import com.example.center_management.domain.entity.LessonProgress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LessonProgressRepository extends JpaRepository<LessonProgress, Long> {

    // Tìm progress của 1 lesson trong 1 enrollment cụ thể
    Optional<LessonProgress> findByEnrollment_IdAndLesson_Id(Long enrollmentId, Long lessonId);

    // Đếm số VIDEO đã hoàn thành trong 1 enrollment
    long countByEnrollment_IdAndCompletedTrueAndLesson_Type(Long enrollmentId, String type);
}

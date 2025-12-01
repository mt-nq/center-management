package com.example.center_management.repository;

import com.example.center_management.domain.entity.LessonProgress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LessonProgressRepository extends JpaRepository<LessonProgress, Integer> {

    List<LessonProgress> findByEnrollment_Id(Integer enrollmentId);

    long countByEnrollment_IdAndCompletedTrue(Integer enrollmentId);
}

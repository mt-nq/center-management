package com.example.center_management.repository;

import com.example.center_management.domain.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Integer> {

    List<Lesson> findBySection_IdOrderBySortOrderAsc(Integer sectionId);

    // đếm số lesson của một course (qua section -> chapter -> course)
    long countBySection_Chapter_Course_Id(Integer courseId);
}

package com.example.center_management.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.center_management.domain.entity.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {

    // Search theo title, code, content
    Page<Course> findByTitleContainingIgnoreCaseOrCodeContainingIgnoreCaseOrContentContainingIgnoreCase(
            String title, String code, String content, Pageable pageable
    );

    // Filter theo status
    Page<Course> findByStatus(String status, Pageable pageable);

    // Search + filter
    Page<Course> findByStatusAndTitleContainingIgnoreCaseOrCodeContainingIgnoreCaseOrContentContainingIgnoreCase(
            String status, String title, String code, String content, Pageable pageable
    );
}

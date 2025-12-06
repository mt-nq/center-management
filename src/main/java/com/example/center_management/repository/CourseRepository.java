package com.example.center_management.repository;

import com.example.center_management.domain.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {

    // Projection cho native query
    interface CourseYearStatProjection {
        Long getCourseId();
        String getCourseTitle();
        LocalDate getStartDate();
        Long getTotalStudents();
        Long getPassedCount();
        Long getFailedCount();
    }

    @Query(value = """
            SELECT 
                c.id AS course_id,
                c.title AS course_title,
                c.start_date,
                COUNT(e.id) AS total_students,
                SUM(CASE WHEN e.completion_result = 'PASSED' THEN 1 ELSE 0 END) AS passed_count,
                SUM(CASE WHEN e.completion_result = 'FAILED' THEN 1 ELSE 0 END) AS failed_count
            FROM courses c
            LEFT JOIN enrollments e ON e.course_id = c.id
            WHERE EXTRACT(YEAR FROM c.start_date) = :year
            GROUP BY c.id, c.title, c.start_date
            ORDER BY c.start_date ASC
            """,
            nativeQuery = true)
    List<CourseYearStatProjection> findCourseStatsByYear(@Param("year") int year);
}

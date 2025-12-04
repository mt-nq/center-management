package com.example.center_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.center_management.domain.entity.Student;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query("""
        SELECT s FROM Student s
        WHERE s.status = 'ACTIVE'
          AND (
                LOWER(s.fullName) LIKE LOWER(CONCAT('%', :keyword, '%'))
             OR LOWER(s.code)     LIKE LOWER(CONCAT('%', :keyword, '%'))
          )
        """)
    Page<Student> searchActiveStudents(@Param("keyword") String keyword, Pageable pageable);

    @Query("""
           SELECT s FROM Student s 
           LEFT JOIN s.enrollments e 
           WHERE e IS NULL
           """)
    List<Student> findStudentsWithoutAnyEnrollment();
    @Query("SELECT s FROM Student s WHERE s.user.id = :userId")
    Optional<Student> findByUserId(Long userId);

}

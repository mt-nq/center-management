package com.example.center_management.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.center_management.domain.entity.Enrollment;
import com.example.center_management.domain.enums.CompletionResult;
import com.example.center_management.domain.enums.EnrollmentStatus;
import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

  @Query("""
      SELECT e FROM Enrollment e
      WHERE e.status = :status
        AND e.completionResult = :result
      ORDER BY e.id DESC
  """)
  Page<Enrollment> getEnrollmentsWithFullProgress(
          @Param("status") EnrollmentStatus status,
          @Param("result") CompletionResult result,
          Pageable pageable
  );

  List<Enrollment> findByStudent_Id(Long studentId);

    // Simple existence check
  boolean existsByStudentIdAndCourseIdAndStatusIn(Long studentId, Long courseId, List<EnrollmentStatus> statuses);

  // Nếu muốn query rõ ràng
  @Query("select case when count(e) > 0 then true else false end from Enrollment e where e.student.id = :studentId and e.course.id = :courseId and e.status in :statuses")
  boolean existsActiveEnrollment(@Param("studentId") Long studentId,
                                  @Param("courseId") Long courseId,
                                  @Param("statuses") List<EnrollmentStatus> statuses);
}

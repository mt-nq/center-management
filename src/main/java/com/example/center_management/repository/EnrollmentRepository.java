package com.example.center_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.example.center_management.domain.entity.Enrollment;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    List<Enrollment> findByStudentId(Long studentId);
    // Có thể thêm các method tìm kiếm custom sau này
}

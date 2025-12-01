package com.example.center_management.repository;

import com.example.center_management.domain.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {

    List<Enrollment> findByStudent_Id(Integer studentId);
}

package com.example.center_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.center_management.domain.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
}

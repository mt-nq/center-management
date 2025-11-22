package com.example.center_management.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.center_management.domain.entity.Student;
import com.example.center_management.dto.request.StudentCreateRequest;
import com.example.center_management.dto.request.StudentUpdateRequest;
import com.example.center_management.dto.response.StudentResponse;
import com.example.center_management.exception.ResourceNotFoundException;
import com.example.center_management.repository.StudentRepository;
import com.example.center_management.service.StudentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    @Override
    @Transactional
    public StudentResponse create(StudentCreateRequest request) {
        Student student = Student.builder()
                .fullName(request.getFullName())
                .dob(request.getDob())
                .hometown(request.getHometown())
                .province(request.getProvince())
                .status("ACTIVE")
                .build();

        student.setCode(generateStudentCode());

        Student saved = studentRepository.save(student);
        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public StudentResponse getById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        return toResponse(student);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentResponse> getAll() {
        return studentRepository.findAll()
            .stream()
            .filter(s -> "ACTIVE".equalsIgnoreCase(s.getStatus()))
            .map(this::toResponse)
            .toList();
    }

    @Override
    @Transactional
    public StudentResponse update(Long id, StudentUpdateRequest request) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        student.setFullName(request.getFullName());
        student.setDob(request.getDob());
        student.setHometown(request.getHometown());
        student.setProvince(request.getProvince());

        return toResponse(student);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Student not found");
        }
        studentRepository.deleteById(id);
    }

@Autowired
private JdbcTemplate jdbcTemplate;

private String generateStudentCode() {
    int year = LocalDate.now().getYear();
    Long nextVal = jdbcTemplate.queryForObject("SELECT nextval('student_seq')", Long.class);
    return String.format("STU-%d-%04d", year, nextVal);
}
    private StudentResponse toResponse(Student s) {
        StudentResponse res = new StudentResponse();
        res.setId(s.getId());
        res.setCode(s.getCode());
        res.setFullName(s.getFullName());
        res.setDob(s.getDob());
        res.setHometown(s.getHometown());
        res.setProvince(s.getProvince());
        res.setStatus(s.getStatus());
        res.setCreatedAt(s.getCreatedAt());
        res.setUpdatedAt(s.getUpdatedAt());

        return res;
    }

    
}

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

    @Autowired
    private JdbcTemplate jdbcTemplate;

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
        return toStudentResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public StudentResponse getById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        return toStudentResponse(student);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentResponse> getAll() {
        return studentRepository.findAll()
                .stream()
                .filter(s -> "ACTIVE".equalsIgnoreCase(s.getStatus()))
                .map(this::toStudentResponse)
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

        Student saved = studentRepository.save(student);
        return toStudentResponse(saved);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        // Soft delete: chỉ đổi trạng thái
        student.setStatus("INACTIVE");
        studentRepository.save(student);
    }

    private String generateStudentCode() {
        int year = LocalDate.now().getYear();
        Long nextVal = jdbcTemplate.queryForObject("SELECT nextval('student_seq')", Long.class);
        return String.format("STU-%d-%04d", year, nextVal);
    }

    private StudentResponse toStudentResponse(Student student) {
        StudentResponse res = new StudentResponse();
        res.setId(student.getId());
        res.setStudentCode(student.getCode());
        res.setFullName(student.getFullName());
        res.setDob(student.getDob());
        res.setHometown(student.getHometown());
        res.setProvince(student.getProvince());
        res.setStatus(student.getStatus());
        return res;
    }
}

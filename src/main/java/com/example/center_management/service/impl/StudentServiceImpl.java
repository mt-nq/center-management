package com.example.center_management.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.example.center_management.domain.entity.Student;
import com.example.center_management.domain.entity.User;
import com.example.center_management.dto.auth.StudentRegisterRequest;
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
    private final JdbcTemplate jdbcTemplate;

    // ================== AUTO TẠO STUDENT KHI ĐĂNG KÝ USER ==================
    @Override
    @Transactional
    public Student createForUser(User user, StudentRegisterRequest request) {
        Student student = Student.builder()
            .fullName(request.getFullName() != null ? request.getFullName() : user.getFullName())
            .dob(request.getDob())
            .hometown(request.getHometown())
            .province(request.getProvince())
            .status("ACTIVE")
            .user(user)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();


        // set code không được null vì DB NOT NULL
        student.setCode(generateStudentCode());

        // trả về entity Student (đúng với interface)
        return studentRepository.save(student);
    }

    // dùng cho login: tìm student theo userId
    @Override
    @Transactional(readOnly = true)
    public Optional<Student> findByUserId(Long userId) {
        return studentRepository.findByUserId(userId);
    }

    // ================== CÁC HÀM KHÁC ==================
    @Override
    @Transactional(readOnly = true)
    public StudentResponse getById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        return toStudentResponse(student);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StudentResponse> getAll(int page, int size) {

        List<StudentResponse> allActiveStudents = studentRepository.findAll()
                .stream()
                .filter(s -> "ACTIVE".equalsIgnoreCase(s.getStatus()))
                .map(this::toStudentResponse)
                .toList();

        int total = allActiveStudents.size();
        int fromIndex = page * size;

        if (fromIndex >= total) {
            return new PageImpl<>(
                    List.of(),
                    PageRequest.of(page, size),
                    total
            );
        }

        int toIndex = Math.min(fromIndex + size, total);
        List<StudentResponse> pageContent = allActiveStudents.subList(fromIndex, toIndex);

        return new PageImpl<>(
                pageContent,
                PageRequest.of(page, size),
                total
        );
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

        if (student.getUser() != null) {
        res.setEmail(student.getUser().getEmail());
        res.setPhone(student.getUser().getPhone());
        }

        return res;
    }

    @Override
    public Long findStudentIdByUsername(String username) {
        Student student = studentRepository.findByUserUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Student not found for username: " + username
                ));
        return student.getId();
    }

}

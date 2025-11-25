package com.example.center_management.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.center_management.domain.entity.Course;
import com.example.center_management.domain.entity.Enrollment;
import com.example.center_management.domain.entity.Student;
import com.example.center_management.dto.request.EnrollmentCreateRequest;
import com.example.center_management.dto.request.EnrollmentResultUpdateRequest;
import com.example.center_management.dto.response.CertificateResponse;
import com.example.center_management.dto.response.EnrollmentResponse;
import com.example.center_management.exception.ResourceNotFoundException;
import com.example.center_management.repository.CourseRepository;
import com.example.center_management.repository.EnrollmentRepository;
import com.example.center_management.repository.StudentRepository;
import com.example.center_management.service.EnrollmentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    @Override
    @Transactional
    public EnrollmentResponse enroll(EnrollmentCreateRequest request) {
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        Enrollment enrollment = Enrollment.builder()
                .student(student)
                .course(course)
                .status("ENROLLED")
                .enrolledAt(LocalDateTime.now())
                .build();

        Enrollment saved = enrollmentRepository.save(enrollment);
        return toEnrollmentResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnrollmentResponse> getAll() {
        return enrollmentRepository.findAll()
                .stream()
                .map(this::toEnrollmentResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnrollmentResponse> getByStudent(Long studentId) {
        return enrollmentRepository.findAll()
                .stream()
                .filter(e -> e.getStudent().getId().equals(studentId))
                .map(this::toEnrollmentResponse)
                .toList();
    }

    @Override
    @Transactional
    public CertificateResponse updateResult(Long enrollmentId, EnrollmentResultUpdateRequest request) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found"));

        boolean passed = Boolean.TRUE.equals(request.getPassed());
        String result = passed ? "PASSED" : "FAILED";
        enrollment.setResult(result);

        if (passed) {
            if (enrollment.getCertificateCode() == null) {
                enrollment.setCertificateCode(generateCertificateCode());
                enrollment.setCertificateIssuedAt(LocalDateTime.now());
            }
        } else {
            enrollment.setCertificateCode(null);
            enrollment.setCertificateIssuedAt(null);
        }

        Enrollment saved = enrollmentRepository.save(enrollment);
        return toCertificateResponse(saved);
    }

    private String generateCertificateCode() {
        int year = LocalDate.now().getYear();
        String random = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return String.format("CERT-%d-%s", year, random);
    }

    private EnrollmentResponse toEnrollmentResponse(Enrollment e) {
        EnrollmentResponse res = new EnrollmentResponse();
        res.setId(e.getId());

        // Student
        if (e.getStudent() != null) {
            res.setStudentId(e.getStudent().getId());
            res.setStudentCode(e.getStudent().getCode()); // SỬA LẠI
            res.setStudentName(e.getStudent().getFullName());
        }

        // Course
        if (e.getCourse() != null) {
            res.setCourseId(e.getCourse().getId());
            res.setCourseCode(e.getCourse().getCode()); // SỬA LẠI
            res.setCourseTitle(e.getCourse().getTitle());
        }

        res.setEnrolledAt(e.getEnrolledAt());
        res.setResult(e.getResult());
        res.setCertificateNo(e.getCertificateCode());

        return res;
    }

    private CertificateResponse toCertificateResponse(Enrollment e) {
        CertificateResponse res = new CertificateResponse();
        res.setEnrollmentId(e.getId());

        // Student
        if (e.getStudent() != null) {
            res.setStudentId(e.getStudent().getId());
            res.setStudentCode(e.getStudent().getCode()); // SỬA LẠI
            res.setStudentName(e.getStudent().getFullName());
        }

        // Course
        if (e.getCourse() != null) {
            res.setCourseId(e.getCourse().getId());
            res.setCourseCode(e.getCourse().getCode()); // SỬA LẠI
            res.setCourseName(e.getCourse().getTitle());
        }

        res.setResult(e.getResult());
        res.setCertificateCode(e.getCertificateCode());
        res.setIssuedAt(e.getCertificateIssuedAt() != null ? e.getCertificateIssuedAt().toLocalDate() : null);


        return res;
    }
}

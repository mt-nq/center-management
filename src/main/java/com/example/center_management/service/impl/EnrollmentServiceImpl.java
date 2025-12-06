package com.example.center_management.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.center_management.domain.entity.Course;
import com.example.center_management.domain.entity.Enrollment;
import com.example.center_management.domain.entity.Student;
import com.example.center_management.domain.entity.Certificate;
import com.example.center_management.domain.enums.CertificateResult;
import com.example.center_management.domain.enums.CompletionResult;
import com.example.center_management.domain.enums.EnrollmentStatus;
import com.example.center_management.dto.enrollment.EnrollmentCompletionResponse;
import com.example.center_management.dto.request.EnrollmentCreateRequest;
import com.example.center_management.dto.request.EnrollmentResultUpdateRequest;
import com.example.center_management.dto.response.CertificateResponse;
import com.example.center_management.dto.response.EnrollmentProgressResponse;
import com.example.center_management.dto.response.EnrollmentResponse;
import com.example.center_management.dto.response.StudentResponse;
import com.example.center_management.exception.ResourceNotFoundException;
import com.example.center_management.repository.CertificateRepository;
import com.example.center_management.repository.CourseRepository;
import com.example.center_management.repository.EnrollmentRepository;
import com.example.center_management.repository.StudentRepository;
import com.example.center_management.service.CertificateService;
import com.example.center_management.service.EnrollmentService;
import com.example.center_management.service.ProgressService;
import com.example.center_management.dto.response.StudentLearningHistoryResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final CertificateService certificateService;
    private final ProgressService progressService; 
    private final CertificateRepository certificateRepository;

    // ================== ENROLL HỌC VIÊN ==================
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
                .status(EnrollmentStatus.ENROLLED)   
                .enrolledAt(LocalDateTime.now())
                .build();

        Enrollment saved = enrollmentRepository.save(enrollment);
        return toEnrollmentResponse(saved);
    }

    // ================== LẤY DANH SÁCH ENROLLMENT ==================
    @Override
    @Transactional(readOnly = true)
    public Page<EnrollmentResponse> getAll(int page, int size) {
        List<EnrollmentResponse> allEnrollments = enrollmentRepository.findAll()
                .stream()
                .map(this::toEnrollmentResponse)
                .toList();

        int total = allEnrollments.size();
        int fromIndex = page * size;

        if (fromIndex >= total) {
            // nếu page quá lớn, trả về trang rỗng
            return new PageImpl<>(
                    List.of(),
                    PageRequest.of(page, size),
                    total
            );
        }

        int toIndex = Math.min(fromIndex + size, total);
        List<EnrollmentResponse> pageContent = allEnrollments.subList(fromIndex, toIndex);

        return new PageImpl<>(
                pageContent,
                PageRequest.of(page, size),
                total
        );
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
        boolean passed = Boolean.TRUE.equals(request.getPassed());
        CertificateResult result = passed ? CertificateResult.PASS : CertificateResult.FAIL;
        // Logic kiểm tra COMPLETED và tạo certificate nằm trong CertificateServiceImpl
        return certificateService.issueCertificate(enrollmentId, result);
    }

    // ================== LẤY DANH SÁCH HỌC SINH CHƯA TỪNG ĐĂNG KÝ ==================
    @Override
    @Transactional(readOnly = true)
    public List<StudentResponse> getStudentsNotEnrolled() {
        List<Student> students = studentRepository.findStudentsWithoutAnyEnrollment();
        return students.stream()
                .map(this::toStudentResponse)
                .toList();
    }

    private StudentResponse toStudentResponse(Student student) {
        StudentResponse dto = new StudentResponse();
        dto.setId(student.getId());

        // DTO dùng field studentCode, entity dùng field code
        dto.setStudentCode(student.getCode());

        dto.setFullName(student.getFullName());
        dto.setDob(student.getDob());
        dto.setHometown(student.getHometown());
        dto.setProvince(student.getProvince());
        dto.setStatus(student.getStatus());

        return dto;
    }

    // ================== MAP ENROLLMENT → DTO ==================
    private EnrollmentResponse toEnrollmentResponse(Enrollment e) {
        EnrollmentResponse res = new EnrollmentResponse();
        res.setId(e.getId());

        // Student
        res.setStudentId(e.getStudent().getId());
        res.setStudentCode(e.getStudent().getCode());
        res.setStudentName(e.getStudent().getFullName());

        // Course
        res.setCourseId(e.getCourse().getId());
        res.setCourseCode(e.getCourse().getCode());
        res.setCourseTitle(e.getCourse().getTitle());

        // Enrollment info
        res.setEnrolledAt(e.getEnrolledAt());
        res.setStatus(e.getStatus());

        try {
            EnrollmentProgressResponse progress = progressService.getProgress(e.getId());
            double pct = progress != null ? progress.getProgressPercentage() : 0.0;
            res.setProgressPercentage(pct);
        } catch (Exception ex) {
            res.setProgressPercentage(0.0);
        }

        Certificate cert = e.getCertificate();
        if (cert != null) {
            // dùng enum CertificateResult trực tiếp
            res.setResult(cert.getResult());                     // PASS / FAIL
            res.setCertificateCode(cert.getCertificateCode());   // CER-00003
        } else {
            // chưa có certificate
            res.setResult(null);            // hoặc e.getCompletionResult(), tùy bạn
            res.setCertificateCode(null);
        }

        return res;
    }

    // ================== CHECK & UPDATE COMPLETION STATUS ==================
    @Override
    @Transactional(readOnly = true)
    public EnrollmentCompletionResponse checkCompletion(Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new NoSuchElementException("Enrollment not found"));

        // Trả luôn DTO đầy đủ, không cần tự chế constructor 4 tham số
        return toCompletionResponse(enrollment);
    }


    // ================== ADMIN DUYỆT KẾT QUẢ PASSED / FAILED ==================
    @Override
    @Transactional
    public EnrollmentCompletionResponse updateCompletionResult(Long enrollmentId,
                                                            CompletionResult result) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Enrollment not found with id: " + enrollmentId));

        // 🔴 BẮT BUỘC: phải hoàn thành trước khi chấm PASS/FAIL
        if (enrollment.getStatus() != EnrollmentStatus.COMPLETED) {
            throw new IllegalStateException(
                    "Enrollment chưa hoàn thành khóa học, không thể chấm PASS/FAIL"
            );
        }

        enrollment.setCompletionResult(result);

        Enrollment saved = enrollmentRepository.save(enrollment);

        certificateService.syncFromEnrollment(saved);

        return toCompletionResponse(saved);
    }


    private EnrollmentCompletionResponse toCompletionResponse(Enrollment enrollment) {
        EnrollmentCompletionResponse dto = new EnrollmentCompletionResponse();

        dto.setEnrollmentId(enrollment.getId());
        dto.setCourseId(enrollment.getCourse().getId());
        dto.setCourseTitle(enrollment.getCourse().getTitle());
        dto.setStudentId(enrollment.getStudent().getId());
        dto.setStudentName(enrollment.getStudent().getFullName());

        dto.setStatus(enrollment.getStatus());
        dto.setResult(enrollment.getCompletionResult());
        dto.setCompletedAt(enrollment.getCompletionDate());

        // --- Phần certificate ---
        Certificate certificate = certificateRepository
                .findByEnrollment(enrollment)
                .orElse(null);

        // enum của bạn đang là PASS / FAIL → dùng PASS
        if (certificate != null && certificate.getResult() == CertificateResult.PASS) {
            dto.setCertificateIssued(true);
        } else {
            dto.setCertificateIssued(false);
        }

        if (certificate != null) {
            dto.setCertificateCode(certificate.getCertificateCode());
            dto.setCertificateIssuedAt(certificate.getIssuedAt());
        }

        return dto;
    }

    // ================== LẤY DANH SÁCH ENROLLMENT ĐÃ HOÀN THÀNH (FULL PROGRESS) ==================
    @Override
    @Transactional(readOnly = true)
    public Page<EnrollmentCompletionResponse> getEnrollmentsWithFullProgress(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);

        Page<Enrollment> enrollmentPage =
                enrollmentRepository.getEnrollmentsWithFullProgress(
                        EnrollmentStatus.COMPLETED,
                        CompletionResult.PASSED,
                        pageable
                );

        return enrollmentPage.map(this::toCompletionResponse);
    }

        @Override
    @Transactional(readOnly = true)
    public StudentLearningHistoryResponse getStudentLearningHistory(Long studentId) {
        // 1. Tìm student để lấy thông tin + check tồn tại
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Student not found with id = " + studentId
                ));

        // 2. Lấy danh sách enrollment của student (dùng method getByStudent đã có)
        List<EnrollmentResponse> enrollments = getByStudent(studentId);

        int total = enrollments.size();

        // 3. Tính toán thống kê
        int completed = (int) enrollments.stream()
                .filter(e -> e.getStatus() == EnrollmentStatus.COMPLETED)
                .count();

        int certificateCount = (int) enrollments.stream()
                .filter(e -> e.getCertificateCode() != null)
                .count();

        int passedCount = (int) enrollments.stream()
                .filter(e -> e.getResult() != null
                        && e.getResult() == CertificateResult.PASS)
                .count();

        int failedCount = (int) enrollments.stream()
                .filter(e -> e.getResult() != null
                        && e.getResult() == CertificateResult.FAIL)
                .count();

        double averageProgress = enrollments.stream()
                .mapToDouble(e -> e.getProgressPercentage() != null
                        ? e.getProgressPercentage()
                        : 0.0)
                .average()
                .orElse(0.0);

        // 4. Build response
        return StudentLearningHistoryResponse.builder()
                .studentId(student.getId())
                .studentCode(student.getCode())
                .studentName(student.getFullName())
                .totalEnrollments(total)
                .completedEnrollments(completed)
                .certificateCount(certificateCount)
                .passedCount(passedCount)
                .failedCount(failedCount)
                .averageProgress(averageProgress)
                .enrollments(enrollments)
                .build();
    }


}

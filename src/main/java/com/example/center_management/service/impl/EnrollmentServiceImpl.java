package com.example.center_management.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.center_management.domain.entity.Course;
import com.example.center_management.domain.entity.Enrollment;
import com.example.center_management.domain.entity.Student;
import com.example.center_management.domain.entity.Certificate;
import com.example.center_management.domain.enums.CertificateResult;
import com.example.center_management.domain.enums.EnrollmentStatus;
import com.example.center_management.dto.enrollment.EnrollmentCompletionResponse;
import com.example.center_management.dto.request.EnrollmentCreateRequest;
import com.example.center_management.dto.request.EnrollmentResultUpdateRequest;
import com.example.center_management.dto.certificate.CertificateResponse;
import com.example.center_management.dto.response.EnrollmentResponse;
import com.example.center_management.dto.response.StudentResponse;
import com.example.center_management.exception.ResourceNotFoundException;
import com.example.center_management.repository.CourseRepository;
import com.example.center_management.repository.EnrollmentRepository;
import com.example.center_management.repository.StudentRepository;
import com.example.center_management.service.CertificateService;
import com.example.center_management.service.EnrollmentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final CertificateService certificateService;

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

    // ================== LẤY DANH SÁCH ENROLLMENT (FIX LỖI VÀ TỐI ƯU PHÂN TRANG) ==================
    /**
     * FIX: Thêm tham số `sortBy` để khớp với chữ ký phương thức trong
     * EnrollmentService và sử dụng phân trang của Spring Data JPA thay vì phân
     * trang thủ công.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<EnrollmentResponse> getAll(int page, int size, String sortBy) { // <--- ĐÃ FIX LỖI BIÊN DỊCH
        // Định nghĩa phân trang và sắp xếp
        // Mặc định sắp xếp theo ID nếu sortBy là null hoặc rỗng
        String sortField = (sortBy != null && !sortBy.isEmpty()) ? sortBy : "id";

        // Tạo Pageable object (PageRequest)
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortField).descending());

        // Sử dụng findAll(Pageable) của Spring Data JPA để phân trang hiệu quả
        Page<Enrollment> enrollmentPage = enrollmentRepository.findAll(pageable);

        // Chuyển đổi Page<Enrollment> sang Page<EnrollmentResponse>
        return enrollmentPage.map(this::toEnrollmentResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnrollmentResponse> getByStudent(Long studentId) {
        // Tối ưu hóa: Thay vì findAll() rồi filter, nên dùng phương thức Repository
        // Giả định EnrollmentRepository có phương thức findByStudent_Id(Long studentId)
        List<Enrollment> enrollments = enrollmentRepository.findByStudentId(studentId);

        return enrollments.stream()
                .map(this::toEnrollmentResponse)
                .toList();
    }

    // ================== CẬP NHẬT KẾT QUẢ CHỨNG CHỈ ==================
    /**
     * Thiết kế mới: - Không còn result / certificateCode / certificateIssuedAt
     * trong Enrollment. - Chứng chỉ được tách sang bảng Certificate. - Ở đây ta
     * chỉ chuyển request (passed = true/false) thành PASS/FAIL rồi gọi sang
     * CertificateService.issueCertificate(...)
     */
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

        // Thông tin chứng chỉ (nếu đã có Certificate)
        Certificate cert = e.getCertificate();
        if (cert != null) {
            // Giả sử EnrollmentResponse có field result (String) & certificateNo
            res.setResult(cert.getResult() != null ? cert.getResult().name() : null);
            res.setCertificateNo(cert.getCertificateNo());
        } else {
            res.setResult(null);
            res.setCertificateNo(null);
        }

        return res;
    }

    // ================== CHECK & UPDATE COMPLETION STATUS ==================
    @Override
    @Transactional(readOnly = true)
    public EnrollmentCompletionResponse checkCompletion(Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new NoSuchElementException("Enrollment not found"));

        boolean completed = enrollment.getStatus() == EnrollmentStatus.COMPLETED;

        return new EnrollmentCompletionResponse(
                enrollment.getId(),
                enrollment.getStudent().getId(),
                enrollment.getCourse().getId(),
                completed
        );
    }

    @Override
    @Transactional
    public void updateCompletionStatus(Long enrollmentId, boolean completed) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new NoSuchElementException("Enrollment not found"));

        if (completed) {
            enrollment.setStatus(EnrollmentStatus.COMPLETED);
            enrollment.setCompletedAt(LocalDateTime.now());
        } else {
            enrollment.setStatus(EnrollmentStatus.NOT_COMPLETED);
            enrollment.setCompletedAt(null);
        }
    }
}

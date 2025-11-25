package com.example.center_management.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.center_management.domain.entity.Course;
import com.example.center_management.domain.entity.Student;
import com.example.center_management.dto.request.CourseCreateRequest;
import com.example.center_management.dto.request.CourseUpdateRequest;
import com.example.center_management.dto.response.CourseResponse;
import com.example.center_management.exception.BadRequestException;
import com.example.center_management.exception.ResourceNotFoundException;
import com.example.center_management.repository.CourseRepository;
import com.example.center_management.repository.StudentRepository;
import com.example.center_management.service.CourseService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    
    private final StudentRepository studentRepository;

    @Override
    @Transactional
    public CourseResponse create(CourseCreateRequest request) {
        validateDates(request.getStartDate(), request.getEndDate());

        Course course = Course.builder()
                .title(request.getTitle())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .content(request.getContent())
                .status("ACTIVE")
                .build();

        course.setCode(generateCourseCode());

        Course saved = courseRepository.save(course);
        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public CourseResponse getById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        return toResponse(course);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseResponse> getAll() {
        return courseRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public CourseResponse update(Long id, CourseUpdateRequest request) {
        validateDates(request.getStartDate(), request.getEndDate());

        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        course.setTitle(request.getTitle());
        course.setStartDate(request.getStartDate());
        course.setEndDate(request.getEndDate());
        course.setContent(request.getContent());

        return toResponse(course);
    }

  @Override
    @Transactional
    public void delete(Long id) {
        // ❗ Soft delete: không xóa khỏi DB, chỉ INACTIVE
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        // Nếu muốn, có thể check xem đã INACTIVE chưa
        if ("INACTIVE".equalsIgnoreCase(course.getStatus())) {
            return; // hoặc throw exception tùy nghiệp vụ
        }

        course.setStatus("INACTIVE");
        courseRepository.save(course);
    }
    private void validateDates(LocalDate start, LocalDate end) {
        if (end.isBefore(start)) {
            throw new BadRequestException("End date must be after start date");
        }
    }

    private String generateCourseCode() {
        long count = courseRepository.count() + 1;
        return String.format("C-%04d", count);
    }

    private CourseResponse toResponse(Course c) {
        CourseResponse res = new CourseResponse();
        res.setId(c.getId());
        res.setCode(c.getCode());
        res.setTitle(c.getTitle());
        res.setStartDate(c.getStartDate());
        res.setEndDate(c.getEndDate());
        res.setContent(c.getContent());
        res.setStatus(c.getStatus());
        res.setCreatedAt(c.getCreatedAt());
        res.setUpdatedAt(c.getUpdatedAt());
        return res;
    }
}

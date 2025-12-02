package com.example.center_management.service.impl;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.center_management.domain.entity.Course;
import com.example.center_management.dto.request.CourseCreateRequest;
import com.example.center_management.dto.request.CourseUpdateRequest;
import com.example.center_management.dto.response.CourseResponse;
import com.example.center_management.exception.BadRequestException;
import com.example.center_management.exception.ResourceNotFoundException;
import com.example.center_management.repository.CourseRepository;
import com.example.center_management.service.CourseService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

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
        if (!courseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Course not found");
        }

        courseRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CourseResponse> getAll(int page, int size, String search, String status) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        Page<Course> courses;

        boolean hasSearch = search != null && !search.isEmpty();
        boolean hasStatus = status != null && !status.isEmpty();

        if (!hasSearch && !hasStatus) {
            courses = courseRepository.findAll(pageable);
        } else if (hasSearch && !hasStatus) {
            courses = courseRepository.findByTitleContainingIgnoreCaseOrCodeContainingIgnoreCaseOrContentContainingIgnoreCase(
                    search, search, search, pageable
            );
        } else if (!hasSearch && hasStatus) {
            courses = courseRepository.findByStatus(status.toUpperCase(), pageable);
        } else {
            courses = courseRepository.findByStatusAndTitleContainingIgnoreCaseOrCodeContainingIgnoreCaseOrContentContainingIgnoreCase(
                    status.toUpperCase(), search, search, search, pageable
            );
        }

        return courses.map(this::toResponseWithDynamicStatus);
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

    private CourseResponse toResponseWithDynamicStatus(Course c) {
        CourseResponse res = toResponse(c);
        LocalDate today = LocalDate.now();

        if (today.isBefore(c.getStartDate())) {
            res.setStatus("Sắp diễn ra");
        } else if (!today.isBefore(c.getStartDate()) && !today.isAfter(c.getEndDate())) {
            res.setStatus("Đang diễn ra");
        } else {
            res.setStatus("Đã kết thúc");
        }

        return res;
    }
}

package com.example.center_management.service.impl;

import com.example.center_management.dto.response.CourseYearStatisticResponse;
import com.example.center_management.repository.CourseRepository;
import com.example.center_management.repository.CourseRepository.CourseYearStatProjection;
import com.example.center_management.service.CourseStatisticsService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseStatisticsServiceImpl implements CourseStatisticsService {

    private final CourseRepository courseRepository;

    @Override
    public List<CourseYearStatisticResponse> getCourseStatisticsByYear(int year) {
        List<CourseYearStatProjection> result = courseRepository.findCourseStatsByYear(year);

        return result.stream()
                .map(r -> CourseYearStatisticResponse.builder()
                        .courseId(r.getCourseId())
                        .courseTitle(r.getCourseTitle())
                        .startDate(r.getStartDate())
                        .totalStudents(r.getTotalStudents())
                        .passedCount(r.getPassedCount())
                        .failedCount(r.getFailedCount())
                        .build()
                )
                .collect(Collectors.toList());
    }
}

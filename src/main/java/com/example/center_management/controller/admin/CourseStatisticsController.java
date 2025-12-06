package com.example.center_management.controller.admin;

import com.example.center_management.dto.response.CourseYearStatisticResponse;
import com.example.center_management.service.CourseStatisticsService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/statistics/courses")
@RequiredArgsConstructor
public class CourseStatisticsController {

    private final CourseStatisticsService courseStatisticsService;

    @GetMapping("/by-year")
    public List<CourseYearStatisticResponse> getByYear(@RequestParam int year) {
        return courseStatisticsService.getCourseStatisticsByYear(year);
    }
}

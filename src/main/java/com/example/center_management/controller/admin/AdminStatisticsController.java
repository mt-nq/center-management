package com.example.center_management.controller.admin;

import com.example.center_management.repository.StudentRepository.LocationStat;
import com.example.center_management.service.CourseStatisticsService;
import com.example.center_management.service.StatisticsService;
import com.example.center_management.dto.response.CourseYearStatisticResponse;
import com.example.center_management.service.CourseStatisticsService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/statistics")
@RequiredArgsConstructor
public class AdminStatisticsController {

    private final StatisticsService statisticsService;
    private final CourseStatisticsService courseStatisticsService;

    @GetMapping("/by-hometown")
    public List<LocationStat> byHometown() {
        return statisticsService.getStudentStatsByHometown();
    }


    @GetMapping("/students/by-province")
    public List<LocationStat> byProvince() {
        return statisticsService.getStudentStatsByProvince();
    }

    @GetMapping("/courses/by-year")
    public List<CourseYearStatisticResponse> getByYear(@RequestParam int year) {
        return courseStatisticsService.getCourseStatisticsByYear(year);
    }
}

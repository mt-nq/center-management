package com.example.center_management.controller.admin;

import com.example.center_management.repository.StudentRepository.LocationStat;
import com.example.center_management.service.StatisticsService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/statistics/students")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/by-hometown")
    public List<LocationStat> byHometown() {
        return statisticsService.getStudentStatsByHometown();
    }

    @GetMapping("/by-province")
    public List<LocationStat> byProvince() {
        return statisticsService.getStudentStatsByProvince();
    }
}

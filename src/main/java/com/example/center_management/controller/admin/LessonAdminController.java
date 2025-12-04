package com.example.center_management.controller.admin;

import com.example.center_management.dto.request.LessonCreateRequest;
import com.example.center_management.dto.response.LessonResponse;
import com.example.center_management.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/chapters")
@RequiredArgsConstructor
public class LessonAdminController {

    private final CourseService courseService;

    @PostMapping("/{chapterId}/lessons")
    public LessonResponse addLesson(@PathVariable Long chapterId,
                                    @Valid @RequestBody LessonCreateRequest request) {
        return courseService.addLesson(chapterId, request);
    }
}

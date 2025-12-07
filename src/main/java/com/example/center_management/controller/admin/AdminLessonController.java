    package com.example.center_management.controller.admin;

    import com.example.center_management.dto.request.LessonCreateRequest;
    import com.example.center_management.dto.response.LessonResponse;
    import com.example.center_management.service.CourseService;
    import com.example.center_management.dto.request.LessonUpdateRequest;
    import jakarta.validation.Valid;
    import lombok.RequiredArgsConstructor;
    import org.springframework.web.bind.annotation.*;

    @RestController
    @RequestMapping("/api/admin/chapters")
    @RequiredArgsConstructor
    public class AdminLessonController {

    private final CourseService courseService;

    @PostMapping("/{chapterId}/lessons")
    public LessonResponse addLesson(@PathVariable Long chapterId,
        @Valid @RequestBody LessonCreateRequest request) {
        return courseService.addLesson(chapterId, request);
    }
        // Sửa bài học
    @PutMapping("/lessons/{lessonId}")
    public LessonResponse updateLesson(@PathVariable Long lessonId,
        @Valid @RequestBody LessonUpdateRequest request) {
        return courseService.updateLesson(lessonId, request);
    }

    // Xóa bài học
    @DeleteMapping("/lessons/{lessonId}")
    public void deleteLesson(@PathVariable Long lessonId) {
        courseService.deleteLesson(lessonId);
    }
    }

package com.example.center_management.service.impl;

import java.time.LocalDate;
import java.util.List;

import com.example.center_management.domain.entity.Chapter;
import com.example.center_management.domain.entity.Course;
import com.example.center_management.domain.entity.Lesson;
import com.example.center_management.dto.request.ChapterCreateRequest;
import com.example.center_management.dto.request.CourseCreateRequest;
import com.example.center_management.dto.request.CourseUpdateRequest;
import com.example.center_management.dto.request.LessonCreateRequest;
import com.example.center_management.dto.response.ChapterResponse;
import com.example.center_management.dto.response.CourseResponse;
import com.example.center_management.dto.response.CourseStructureResponse;
import com.example.center_management.dto.response.LessonResponse;
import com.example.center_management.exception.BadRequestException;
import com.example.center_management.exception.ResourceNotFoundException;
import com.example.center_management.repository.ChapterRepository;
import com.example.center_management.repository.CourseRepository;
import com.example.center_management.repository.LessonRepository;
import com.example.center_management.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final ChapterRepository chapterRepository;
    private final LessonRepository lessonRepository;

    // ============================================================
    // CRUD cơ bản cho Course
    // ============================================================

    @Override
    @Transactional
    public CourseResponse create(CourseCreateRequest request) {
        // Chỉ validate khi cả 2 ngày đều khác null
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
    public Page<CourseResponse> getAll(int page, int size) {

        List<CourseResponse> allActiveCourses = courseRepository.findAll()
                .stream()
                .filter(s -> "ACTIVE".equalsIgnoreCase(s.getStatus()))
                .map(this::toResponse)
                .toList();

        int total = allActiveCourses.size();
        int fromIndex = page * size;

        if (fromIndex >= total) {
            return new PageImpl<>(
                    List.of(),
                    PageRequest.of(page, size),
                    total
            );
        }

        int toIndex = Math.min(fromIndex + size, total);
        List<CourseResponse> pageContent = allActiveCourses.subList(fromIndex, toIndex);

        return new PageImpl<>(
                pageContent,
                PageRequest.of(page, size),
                total
        );
    }

    @Override
    @Transactional
    public CourseResponse update(Long id, CourseUpdateRequest request) {
        // CourseUpdateRequest nhiều khả năng vẫn có start/endDate,
        // nên ta cũng dùng validateDates nhưng an toàn với null
        validateDates(request.getStartDate(), request.getEndDate());

        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        course.setTitle(request.getTitle());
        course.setStartDate(request.getStartDate());
        course.setEndDate(request.getEndDate());
        course.setContent(request.getContent());

        Course updated = courseRepository.saveAndFlush(course);

        return toResponse(updated);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        // ❗ Soft delete: không xóa khỏi DB, chỉ INACTIVE
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("course not found"));

        if ("INACTIVE".equalsIgnoreCase(course.getStatus())) {
            return; // hoặc throw exception tùy nghiệp vụ
        }

        course.setStatus("INACTIVE");
        courseRepository.save(course);
    }

    // ============================================================
    // API mới: structure, addChapter, addLesson
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public CourseStructureResponse getStructure(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        return toCourseStructure(course);
    }

    @Override
    @Transactional
    public ChapterResponse addChapter(Long courseId, ChapterCreateRequest request) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        Chapter chapter = Chapter.builder()
                .title(request.getTitle())
                .course(course)
                .build();

        chapterRepository.save(chapter);

        return toChapterResponse(chapter);
    }

    @Override
    @Transactional
    public LessonResponse addLesson(Long chapterId, LessonCreateRequest request) {
        Chapter chapter = chapterRepository.findById(chapterId)
                .orElseThrow(() -> new ResourceNotFoundException("Chapter not found"));

        Lesson lesson = Lesson.builder()
                .title(request.getTitle())
                .type(request.getType())
                .chapter(chapter)
                .build();

        lessonRepository.save(lesson);

        return toLessonResponse(lesson);
    }

    // ============================================================
    // Helper methods
    // ============================================================

    private void validateDates(LocalDate start, LocalDate end) {
        // Nếu 1 trong 2 null thì bỏ qua, không validate
        if (start == null || end == null) {
            return;
        }

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

    private CourseStructureResponse toCourseStructure(Course course) {
        return CourseStructureResponse.builder()
                .id(course.getId())
                .title(course.getTitle())
                .description(course.getContent())
                .chapters(
                        course.getChapters().stream()
                                .map(this::toChapterResponse)
                                .toList()
                )
                .build();
    }

    private ChapterResponse toChapterResponse(Chapter chapter) {
        return ChapterResponse.builder()
                .id(chapter.getId())
                .title(chapter.getTitle())
                .lessons(
                        chapter.getLessons().stream()
                                .map(this::toLessonResponse)
                                .toList()
                )
                .build();
    }

    private LessonResponse toLessonResponse(Lesson lesson) {
        return LessonResponse.builder()
                .id(lesson.getId())
                .title(lesson.getTitle())
                .type(lesson.getType())
                .build();
    }
}

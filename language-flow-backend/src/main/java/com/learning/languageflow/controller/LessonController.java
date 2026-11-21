package com.learning.languageflow.controller;

import com.learning.languageflow.dto.ApiResponse;
import com.learning.languageflow.dto.CompleteLessonRequest;
import com.learning.languageflow.dto.CourseProgressResponse;
import com.learning.languageflow.dto.DtoMapper;
import com.learning.languageflow.dto.LessonDto;
import com.learning.languageflow.model.Lesson;
import com.learning.languageflow.service.LessonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/lessons")
@CrossOrigin(origins = "*")
public class LessonController {

    private final LessonService lessonService;
    private final DtoMapper dtoMapper;

    public LessonController(LessonService lessonService, DtoMapper dtoMapper) {
        this.lessonService = lessonService;
        this.dtoMapper = dtoMapper;
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<ApiResponse<List<LessonDto>>> getLessonsByCourse(@PathVariable int courseId) {
        List<Lesson> lessons = lessonService.getLessonsByCourse(courseId);
        List<LessonDto> dtos = lessons.stream()
                .map(dtoMapper::toLessonDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.ok("Lessons fetched successfully", dtos));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<LessonDto>> getLessonById(@PathVariable int id) {
        try {
            Lesson lesson = lessonService.getLessonById(id);
            return ResponseEntity.ok(ApiResponse.ok(dtoMapper.toLessonDto(lesson)));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(ex.getMessage()));
        }
    }

    @GetMapping("/course/{courseId}/offset/{offset}")
    public ResponseEntity<ApiResponse<LessonDto>> getLessonByOffset(@PathVariable int courseId, @PathVariable int offset) {
        Optional<Lesson> lessonOpt = lessonService.getLessonByOffset(courseId, offset);
        return lessonOpt.map(lesson -> ResponseEntity.ok(ApiResponse.ok(dtoMapper.toLessonDto(lesson))))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("No lesson found at offset: " + offset)));
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<ApiResponse<Double>> completeLesson(@PathVariable int id, @RequestBody CompleteLessonRequest request) {
        try {
            double newProgress = lessonService.completeLesson(request.getUsername(), id, request.getCourseId());
            return ResponseEntity.ok(ApiResponse.ok("Lesson marked as completed", newProgress));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        }
    }

    @GetMapping("/course/{courseId}/progress")
    public ResponseEntity<ApiResponse<CourseProgressResponse>> getCourseProgress(
            @PathVariable int courseId,
            @RequestParam String username) {
        CourseProgressResponse progress = lessonService.getCourseProgress(username, courseId);
        return ResponseEntity.ok(ApiResponse.ok(progress));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<LessonDto>> createLesson(@RequestBody LessonDto lessonDto) {
        try {
            Lesson lesson = dtoMapper.toLessonEntity(lessonDto);
            Lesson created = lessonService.createLesson(lesson);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.ok("Lesson created successfully", dtoMapper.toLessonDto(created)));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<LessonDto>> updateLesson(@PathVariable int id, @RequestBody LessonDto lessonDto) {
        try {
            Lesson lesson = dtoMapper.toLessonEntity(lessonDto);
            Lesson updated = lessonService.updateLesson(id, lesson);
            return ResponseEntity.ok(ApiResponse.ok("Lesson updated successfully", dtoMapper.toLessonDto(updated)));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(ex.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteLesson(@PathVariable int id) {
        boolean deleted = lessonService.deleteLesson(id);
        if (deleted) {
            return ResponseEntity.ok(ApiResponse.ok("Lesson deleted successfully", null));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Lesson not found with id: " + id));
        }
    }
}

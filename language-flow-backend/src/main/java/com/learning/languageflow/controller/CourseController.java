package com.learning.languageflow.controller;

import com.learning.languageflow.dto.ApiResponse;
import com.learning.languageflow.dto.CourseDto;
import com.learning.languageflow.dto.DtoMapper;
import com.learning.languageflow.model.Course;
import com.learning.languageflow.service.CourseService;
import com.learning.languageflow.service.LessonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/courses")
@CrossOrigin(origins = "*")
public class CourseController {

    private final CourseService courseService;
    private final LessonService lessonService;
    private final DtoMapper dtoMapper;

    public CourseController(CourseService courseService, LessonService lessonService, DtoMapper dtoMapper) {
        this.courseService = courseService;
        this.lessonService = lessonService;
        this.dtoMapper = dtoMapper;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CourseDto>>> getAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        List<CourseDto> dtos = courses.stream()
                .map(c -> dtoMapper.toCourseDto(c, lessonService.getTotalLessonsCount(c.getId())))
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.ok("Courses fetched successfully", dtos));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CourseDto>> getCourseById(@PathVariable int id) {
        try {
            Course course = courseService.getCourseById(id);
            int totalLessons = lessonService.getTotalLessonsCount(id);
            return ResponseEntity.ok(ApiResponse.ok(dtoMapper.toCourseDto(course, totalLessons)));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(ex.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CourseDto>> createCourse(@RequestBody CourseDto courseDto) {
        try {
            Course course = dtoMapper.toCourseEntity(courseDto);
            Course created = courseService.createCourse(course);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.ok("Course created successfully", dtoMapper.toCourseDto(created, 0)));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CourseDto>> updateCourse(@PathVariable int id, @RequestBody CourseDto courseDto) {
        try {
            Course course = dtoMapper.toCourseEntity(courseDto);
            Course updated = courseService.updateCourse(id, course);
            int total = lessonService.getTotalLessonsCount(id);
            return ResponseEntity.ok(ApiResponse.ok("Course updated successfully", dtoMapper.toCourseDto(updated, total)));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(ex.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCourse(@PathVariable int id) {
        boolean deleted = courseService.deleteCourse(id);
        if (deleted) {
            return ResponseEntity.ok(ApiResponse.ok("Course deleted successfully", null));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Course not found with id: " + id));
        }
    }
}

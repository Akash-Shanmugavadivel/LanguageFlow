package com.learning.languageflow.controller;

import com.learning.languageflow.dto.ApiResponse;
import com.learning.languageflow.dto.DtoMapper;
import com.learning.languageflow.dto.QuizQuestionAdminDto;
import com.learning.languageflow.dto.QuizQuestionDto;
import com.learning.languageflow.dto.QuizSubmissionRequest;
import com.learning.languageflow.model.QuizQuestion;
import com.learning.languageflow.model.QuizResult;
import com.learning.languageflow.service.QuizService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/quizzes")
@CrossOrigin(origins = "*")
public class QuizController {

    private final QuizService quizService;
    private final DtoMapper dtoMapper;

    public QuizController(QuizService quizService, DtoMapper dtoMapper) {
        this.quizService = quizService;
        this.dtoMapper = dtoMapper;
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<ApiResponse<List<QuizQuestionDto>>> getQuestionsForCourse(@PathVariable int courseId) {
        List<QuizQuestion> questions = quizService.getQuestionsForCourse(courseId);
        // Map to public DTO (without correctOption) so answers cannot be inspected in client dev tools
        List<QuizQuestionDto> dtos = questions.stream()
                .map(dtoMapper::toQuizQuestionDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.ok("Quiz questions fetched successfully", dtos));
    }

    @GetMapping("/lesson/{lessonId}")
    public ResponseEntity<ApiResponse<List<QuizQuestionDto>>> getQuestionsForLesson(@PathVariable int lessonId) {
        List<QuizQuestion> questions = quizService.getQuestionsForLesson(lessonId);
        List<QuizQuestionDto> dtos = questions.stream()
                .map(dtoMapper::toQuizQuestionDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.ok("Quiz questions fetched successfully", dtos));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<QuizQuestionAdminDto>> getQuestionById(@PathVariable int id) {
        try {
            QuizQuestion question = quizService.getQuestionById(id);
            return ResponseEntity.ok(ApiResponse.ok(dtoMapper.toQuizQuestionAdminDto(question)));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(ex.getMessage()));
        }
    }

    @PostMapping("/course/{courseId}/submit")
    public ResponseEntity<ApiResponse<QuizResult>> submitQuiz(
            @PathVariable int courseId,
            @RequestBody QuizSubmissionRequest request) {
        try {
            QuizResult result = quizService.evaluateQuiz(request.getUsername(), courseId, request.getAnswers());
            return ResponseEntity.ok(ApiResponse.ok("Quiz evaluated successfully", result));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<QuizQuestionAdminDto>> createQuestion(@RequestBody QuizQuestionAdminDto questionDto) {
        try {
            QuizQuestion question = dtoMapper.toQuizQuestionEntity(questionDto);
            QuizQuestion created = quizService.createQuestion(question);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.ok("Quiz question created successfully", dtoMapper.toQuizQuestionAdminDto(created)));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<QuizQuestionAdminDto>> updateQuestion(@PathVariable int id, @RequestBody QuizQuestionAdminDto questionDto) {
        try {
            QuizQuestion question = dtoMapper.toQuizQuestionEntity(questionDto);
            QuizQuestion updated = quizService.updateQuestion(id, question);
            return ResponseEntity.ok(ApiResponse.ok("Quiz question updated successfully", dtoMapper.toQuizQuestionAdminDto(updated)));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(ex.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteQuestion(@PathVariable int id) {
        boolean deleted = quizService.deleteQuestion(id);
        if (deleted) {
            return ResponseEntity.ok(ApiResponse.ok("Quiz question deleted successfully", null));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Quiz question not found with id: " + id));
        }
    }
}

package com.learning.languageflow.dto;

import com.learning.languageflow.model.Course;
import com.learning.languageflow.model.DifficultyLevel;
import com.learning.languageflow.model.Lesson;
import com.learning.languageflow.model.QuizQuestion;
import com.learning.languageflow.model.User;
import org.springframework.stereotype.Component;

@Component
public class DtoMapper {

    public UserDto toUserDto(User user) {
        if (user == null) return null;
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getStreak(),
                user.getDailyGoalMinutes(),
                user.getProgressPercentage(),
                user.getLastLoginDate()
        );
    }

    public User toUserEntity(UserDto dto) {
        if (dto == null) return null;
        return User.builder()
                .id(dto.getId())
                .username(dto.getUsername())
                .streak(dto.getStreak())
                .dailyGoalMinutes(dto.getDailyGoalMinutes())
                .progressPercentage(dto.getProgressPercentage())
                .lastLoginDate(dto.getLastLoginDate())
                .build();
    }

    public CourseDto toCourseDto(Course course, int totalLessons) {
        if (course == null) return null;
        return new CourseDto(
                course.getId(),
                course.getName(),
                course.getDescription(),
                totalLessons
        );
    }

    public Course toCourseEntity(CourseDto dto) {
        if (dto == null) return null;
        return Course.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .build();
    }

    public LessonDto toLessonDto(Lesson lesson) {
        if (lesson == null) return null;
        DifficultyLevel level = lesson.getDifficultyLevel() != null ? lesson.getDifficultyLevel() : DifficultyLevel.BASIC;
        return new LessonDto(
                lesson.getId(),
                lesson.getCourseId(),
                lesson.getTitle(),
                lesson.getContent(),
                level.getLevel(),
                level.getDescription()
        );
    }

    public Lesson toLessonEntity(LessonDto dto) {
        if (dto == null) return null;
        return Lesson.builder()
                .id(dto.getId())
                .courseId(dto.getCourseId())
                .title(dto.getTitle())
                .content(dto.getContent())
                .difficultyLevel(dto.getDifficultyLevel())
                .build();
    }

    public QuizQuestionDto toQuizQuestionDto(QuizQuestion q) {
        if (q == null) return null;
        return new QuizQuestionDto(
                q.getId(),
                q.getLessonId(),
                q.getQuestion(),
                q.getOptionA(),
                q.getOptionB(),
                q.getOptionC(),
                q.getOptionD()
        );
    }

    public QuizQuestionAdminDto toQuizQuestionAdminDto(QuizQuestion q) {
        if (q == null) return null;
        return new QuizQuestionAdminDto(
                q.getId(),
                q.getLessonId(),
                q.getQuestion(),
                q.getOptionA(),
                q.getOptionB(),
                q.getOptionC(),
                q.getOptionD(),
                q.getCorrectOption()
        );
    }

    public QuizQuestion toQuizQuestionEntity(QuizQuestionAdminDto dto) {
        if (dto == null) return null;
        return QuizQuestion.builder()
                .id(dto.getId())
                .lessonId(dto.getLessonId())
                .question(dto.getQuestion())
                .optionA(dto.getOptionA())
                .optionB(dto.getOptionB())
                .optionC(dto.getOptionC())
                .optionD(dto.getOptionD())
                .correctOption(dto.getCorrectOption())
                .build();
    }
}

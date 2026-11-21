package com.learning.languageflow.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Domain Entity representing a structured learning unit within a course.
 */
public class Lesson extends BaseEntity {

    private Integer courseId;
    private String title;
    private String content;
    private DifficultyLevel difficultyLevel;
    private final List<QuizQuestion> questions = new ArrayList<>();

    public Lesson() {
        super();
        this.difficultyLevel = DifficultyLevel.BASIC;
    }

    public Lesson(Integer id, Integer courseId, String title, String content, int difficultyLevel) {
        super(id);
        this.courseId = courseId;
        this.title = title;
        this.content = content;
        this.difficultyLevel = DifficultyLevel.fromLevel(difficultyLevel);
    }

    public Lesson(Integer id, Integer courseId, String title, String content, DifficultyLevel difficultyLevel) {
        super(id);
        this.courseId = courseId;
        this.title = title;
        this.content = content;
        this.difficultyLevel = difficultyLevel != null ? difficultyLevel : DifficultyLevel.BASIC;
    }

    // --- Domain Behaviors ---

    public void addQuestion(QuizQuestion question) {
        if (question != null) {
            if (this.id != null) {
                question.setLessonId(this.id);
            }
            this.questions.add(question);
        }
    }

    public List<QuizQuestion> getQuestions() {
        return Collections.unmodifiableList(questions);
    }

    public boolean hasQuiz() {
        return !questions.isEmpty();
    }

    public boolean isIntroductory() {
        return this.difficultyLevel == DifficultyLevel.BEGINNER;
    }

    // --- Getters and Setters ---

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public DifficultyLevel getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(DifficultyLevel difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public int getDifficultyLevelAsInt() {
        return difficultyLevel != null ? difficultyLevel.getLevel() : 1;
    }

    public void setDifficultyLevelFromInt(int level) {
        this.difficultyLevel = DifficultyLevel.fromLevel(level);
    }

    // --- Builder Pattern ---

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer id;
        private Integer courseId;
        private String title;
        private String content;
        private DifficultyLevel difficultyLevel = DifficultyLevel.BASIC;

        public Builder id(Integer id) {
            this.id = id;
            return this;
        }

        public Builder courseId(Integer courseId) {
            this.courseId = courseId;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder difficultyLevel(DifficultyLevel difficultyLevel) {
            this.difficultyLevel = difficultyLevel;
            return this;
        }

        public Builder difficultyLevel(int level) {
            this.difficultyLevel = DifficultyLevel.fromLevel(level);
            return this;
        }

        public Lesson build() {
            return new Lesson(id, courseId, title, content, difficultyLevel);
        }
    }
}

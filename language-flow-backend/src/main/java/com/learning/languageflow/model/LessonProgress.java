package com.learning.languageflow.model;

/**
 * Domain Entity representing a learner's progress on an individual lesson.
 */
public class LessonProgress extends BaseEntity {

    private String username;
    private Integer lessonId;
    private boolean completed;

    public LessonProgress() {
        super();
    }

    public LessonProgress(Integer id, String username, Integer lessonId, boolean completed) {
        super(id);
        this.username = username;
        this.lessonId = lessonId;
        this.completed = completed;
    }

    public LessonProgress(String username, Integer lessonId, boolean completed) {
        this(null, username, lessonId, completed);
    }

    // --- Domain Behaviors ---

    public void markCompleted() {
        this.completed = true;
    }

    // --- Getters and Setters ---

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getLessonId() {
        return lessonId;
    }

    public void setLessonId(Integer lessonId) {
        this.lessonId = lessonId;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}

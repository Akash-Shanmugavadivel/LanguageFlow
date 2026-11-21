package com.learning.languageflow.dto;

public class LessonDto {

    private Integer id;
    private Integer courseId;
    private String title;
    private String content;
    private int difficultyLevel;
    private String difficultyDescription;

    public LessonDto() {
    }

    public LessonDto(Integer id, Integer courseId, String title, String content, int difficultyLevel, String difficultyDescription) {
        this.id = id;
        this.courseId = courseId;
        this.title = title;
        this.content = content;
        this.difficultyLevel = difficultyLevel;
        this.difficultyDescription = difficultyDescription;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public int getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(int difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public String getDifficultyDescription() {
        return difficultyDescription;
    }

    public void setDifficultyDescription(String difficultyDescription) {
        this.difficultyDescription = difficultyDescription;
    }
}

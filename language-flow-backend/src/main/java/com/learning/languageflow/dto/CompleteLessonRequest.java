package com.learning.languageflow.dto;

public class CompleteLessonRequest {

    private String username;
    private int courseId;

    public CompleteLessonRequest() {
    }

    public CompleteLessonRequest(String username, int courseId) {
        this.username = username;
        this.courseId = courseId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }
}

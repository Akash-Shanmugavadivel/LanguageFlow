package com.learning.languageflow.dto;

import java.util.List;

public class CourseProgressResponse {

    private int courseId;
    private int totalLessons;
    private int completedLessons;
    private double progressPercentage;
    private boolean canTakeQuiz;
    private List<Integer> completedLessonIds;

    public CourseProgressResponse() {
    }

    public CourseProgressResponse(int courseId, int totalLessons, int completedLessons, double progressPercentage, boolean canTakeQuiz, List<Integer> completedLessonIds) {
        this.courseId = courseId;
        this.totalLessons = totalLessons;
        this.completedLessons = completedLessons;
        this.progressPercentage = progressPercentage;
        this.canTakeQuiz = canTakeQuiz;
        this.completedLessonIds = completedLessonIds;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getTotalLessons() {
        return totalLessons;
    }

    public void setTotalLessons(int totalLessons) {
        this.totalLessons = totalLessons;
    }

    public int getCompletedLessons() {
        return completedLessons;
    }

    public void setCompletedLessons(int completedLessons) {
        this.completedLessons = completedLessons;
    }

    public double getProgressPercentage() {
        return progressPercentage;
    }

    public void setProgressPercentage(double progressPercentage) {
        this.progressPercentage = progressPercentage;
    }

    public boolean isCanTakeQuiz() {
        return canTakeQuiz;
    }

    public void setCanTakeQuiz(boolean canTakeQuiz) {
        this.canTakeQuiz = canTakeQuiz;
    }

    public List<Integer> getCompletedLessonIds() {
        return completedLessonIds;
    }

    public void setCompletedLessonIds(List<Integer> completedLessonIds) {
        this.completedLessonIds = completedLessonIds;
    }
}

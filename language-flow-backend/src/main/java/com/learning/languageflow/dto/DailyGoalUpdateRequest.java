package com.learning.languageflow.dto;

public class DailyGoalUpdateRequest {

    private int dailyGoalMinutes;

    public DailyGoalUpdateRequest() {
    }

    public DailyGoalUpdateRequest(int dailyGoalMinutes) {
        this.dailyGoalMinutes = dailyGoalMinutes;
    }

    public int getDailyGoalMinutes() {
        return dailyGoalMinutes;
    }

    public void setDailyGoalMinutes(int dailyGoalMinutes) {
        this.dailyGoalMinutes = dailyGoalMinutes;
    }
}

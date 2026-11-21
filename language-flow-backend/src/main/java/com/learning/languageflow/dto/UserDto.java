package com.learning.languageflow.dto;

import java.time.LocalDate;

public class UserDto {

    private Integer id;
    private String username;
    private int streak;
    private int dailyGoalMinutes;
    private double progressPercentage;
    private LocalDate lastLoginDate;

    public UserDto() {
    }

    public UserDto(Integer id, String username, int streak, int dailyGoalMinutes, double progressPercentage, LocalDate lastLoginDate) {
        this.id = id;
        this.username = username;
        this.streak = streak;
        this.dailyGoalMinutes = dailyGoalMinutes;
        this.progressPercentage = progressPercentage;
        this.lastLoginDate = lastLoginDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getStreak() {
        return streak;
    }

    public void setStreak(int streak) {
        this.streak = streak;
    }

    public int getDailyGoalMinutes() {
        return dailyGoalMinutes;
    }

    public void setDailyGoalMinutes(int dailyGoalMinutes) {
        this.dailyGoalMinutes = dailyGoalMinutes;
    }

    public double getProgressPercentage() {
        return progressPercentage;
    }

    public void setProgressPercentage(double progressPercentage) {
        this.progressPercentage = progressPercentage;
    }

    public LocalDate getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(LocalDate lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }
}

package com.learning.languageflow.model;

import java.time.LocalDate;

/**
 * Rich Domain Entity representing a learner in the platform.
 * Encapsulates domain logic for learning streaks, progress accumulation, and goal tracking.
 */
public class User extends BaseEntity {

    private String username;
    private String password;
    private int streak;
    private int dailyGoalMinutes;
    private double progressPercentage;
    private LocalDate lastLoginDate;

    public User() {
        super();
        this.streak = 0;
        this.dailyGoalMinutes = 15;
        this.progressPercentage = 0.0;
    }

    public User(Integer id, String username, String password, int streak, int dailyGoalMinutes, double progressPercentage) {
        super(id);
        this.username = username;
        this.password = password;
        this.streak = Math.max(0, streak);
        this.dailyGoalMinutes = dailyGoalMinutes > 0 ? dailyGoalMinutes : 15;
        this.progressPercentage = Math.max(0.0, progressPercentage);
    }

    public User(Integer id, String username, String password, int streak, int dailyGoalMinutes, double progressPercentage, LocalDate lastLoginDate) {
        this(id, username, password, streak, dailyGoalMinutes, progressPercentage);
        this.lastLoginDate = lastLoginDate;
    }

    // --- Domain Behaviors (OOP Encapsulation) ---

    public void incrementStreak() {
        this.streak++;
    }

    public void resetStreak() {
        this.streak = 0;
    }

    /**
     * Domain rule: Evaluate and update user streak according to consecutive login days.
     */
    public boolean evaluateStreakOnLogin(LocalDate today) {
        if (this.lastLoginDate == null) {
            incrementStreak();
            this.lastLoginDate = today;
            return true;
        }

        if (this.lastLoginDate.plusDays(1).isEqual(today)) {
            incrementStreak();
            this.lastLoginDate = today;
            return true;
        } else if (!this.lastLoginDate.isEqual(today)) {
            resetStreak();
            this.lastLoginDate = today;
            return false;
        }
        return false;
    }

    /**
     * Domain rule: Accumulate progress with boundary checks.
     */
    public void addProgress(double deltaPercentage) {
        if (deltaPercentage > 0) {
            this.progressPercentage = Math.min(100.0, this.progressPercentage + deltaPercentage);
        }
    }

    public boolean isDailyGoalMet(int minutesSpent) {
        return minutesSpent >= this.dailyGoalMinutes;
    }

    public boolean verifyPassword(String candidatePassword) {
        return this.password != null && this.password.equals(candidatePassword);
    }

    // --- Getters and Setters with Invariants ---

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getStreak() {
        return streak;
    }

    public void setStreak(int streak) {
        this.streak = Math.max(0, streak);
    }

    public int getDailyGoalMinutes() {
        return dailyGoalMinutes;
    }

    public void setDailyGoalMinutes(int dailyGoalMinutes) {
        if (dailyGoalMinutes <= 0) {
            throw new IllegalArgumentException("Daily goal must be a positive number of minutes.");
        }
        this.dailyGoalMinutes = dailyGoalMinutes;
    }

    public double getProgressPercentage() {
        return progressPercentage;
    }

    public void setProgressPercentage(double progressPercentage) {
        this.progressPercentage = Math.max(0.0, progressPercentage);
    }

    public LocalDate getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(LocalDate lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    // --- Builder Pattern ---

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer id;
        private String username;
        private String password;
        private int streak = 0;
        private int dailyGoalMinutes = 15;
        private double progressPercentage = 0.0;
        private LocalDate lastLoginDate;

        public Builder id(Integer id) {
            this.id = id;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder streak(int streak) {
            this.streak = streak;
            return this;
        }

        public Builder dailyGoalMinutes(int dailyGoalMinutes) {
            this.dailyGoalMinutes = dailyGoalMinutes;
            return this;
        }

        public Builder progressPercentage(double progressPercentage) {
            this.progressPercentage = progressPercentage;
            return this;
        }

        public Builder lastLoginDate(LocalDate lastLoginDate) {
            this.lastLoginDate = lastLoginDate;
            return this;
        }

        public User build() {
            return new User(id, username, password, streak, dailyGoalMinutes, progressPercentage, lastLoginDate);
        }
    }
}

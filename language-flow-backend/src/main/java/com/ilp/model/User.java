package com.ilp.model;

/**
 * Legacy compatibility alias for User domain entity.
 */
public class User extends com.learning.languageflow.model.User {
    public User() {
        super();
    }

    public User(Integer id, String username, String password, int streak, int dailyGoalMinutes, double progressPercentage) {
        super(id, username, password, streak, dailyGoalMinutes, progressPercentage);
    }
}

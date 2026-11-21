package com.learning.languageflow.dao;

import com.learning.languageflow.model.User;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Contract for User persistence operations.
 */
public interface UserDao extends GenericDao<User, Integer> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    int updateLastLoginDate(String username, LocalDate date);

    int incrementStreak(String username);

    int resetStreak(String username);

    int updateStreak(String username, int streak);

    int updateDailyGoal(String username, int dailyGoalMinutes);

    int updateProgressPercentage(String username, double progressPercentage);

    int updateProgressAndIncrementStreak(String username, double progressPercentage);
}

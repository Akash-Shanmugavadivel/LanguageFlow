package com.learning.languageflow.service;

import com.learning.languageflow.dao.UserDao;
import com.learning.languageflow.model.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Domain Service for managing learner progression, goals, and daily streaks.
 */
@Service
public class ProgressService {

    private final UserDao userDao;

    public ProgressService(UserDao userDao) {
        this.userDao = userDao;
    }

    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    public User getUserProgress(String username) {
        Optional<User> userOpt = userDao.findByUsername(username);
        return userOpt.orElseThrow(() -> new IllegalArgumentException("User not found: " + username));
    }

    public void updateDailyGoal(String username, int dailyGoalMinutes) {
        User user = getUserProgress(username);
        user.setDailyGoalMinutes(dailyGoalMinutes);
        userDao.updateDailyGoal(username, user.getDailyGoalMinutes());
    }

    public void updateProgressPercentage(String username, double percentage) {
        User user = getUserProgress(username);
        user.setProgressPercentage(percentage);
        userDao.updateProgressPercentage(username, user.getProgressPercentage());
    }

    public void incrementStreak(String username) {
        userDao.incrementStreak(username);
    }

    public void resetStreak(String username) {
        userDao.resetStreak(username);
    }

    public void checkAndUpdateStreak(String username, LocalDate lastLoginDate) {
        User user = getUserProgress(username);
        user.setLastLoginDate(lastLoginDate);
        user.evaluateStreakOnLogin(LocalDate.now());
        userDao.updateStreak(username, user.getStreak());
    }

    public boolean deleteUser(int id) {
        return userDao.deleteById(id);
    }
}

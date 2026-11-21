package com.learning.languageflow.service;

import com.learning.languageflow.dao.UserDao;
import com.learning.languageflow.model.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Domain Service for Authentication and Registration workflows.
 */
@Service
public class AuthService {

    private final UserDao userDao;
    private final ProgressService progressService;

    public AuthService(UserDao userDao, ProgressService progressService) {
        this.userDao = userDao;
        this.progressService = progressService;
    }

    public boolean authenticateUser(String username, String password) {
        if (username == null || username.trim().isEmpty() || password == null || password.isEmpty()) {
            return false;
        }

        Optional<User> userOpt = userDao.findByUsername(username.trim());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // Encapsulated domain logic: password verification
            if (user.verifyPassword(password)) {
                // Encapsulated domain behavior: streak evaluation on login
                LocalDate today = LocalDate.now();
                user.evaluateStreakOnLogin(today);

                userDao.updateStreak(user.getUsername(), user.getStreak());
                userDao.updateLastLoginDate(user.getUsername(), today);
                return true;
            }
        }
        return false;
    }

    public boolean registerUser(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty.");
        }
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }
        if (password.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters long.");
        }
        if (!isValidPassword(password)) {
            throw new IllegalArgumentException("Password must contain at least one uppercase letter, one lowercase letter, and one digit.");
        }
        if (userDao.existsByUsername(username.trim())) {
            return false;
        }

        User newUser = User.builder()
                .username(username.trim())
                .password(password)
                .streak(0)
                .dailyGoalMinutes(15)
                .progressPercentage(0.0)
                .build();

        User saved = userDao.save(newUser);
        return saved != null;
    }

    public boolean isValidPassword(String password) {
        boolean hasLower = false;
        boolean hasUpper = false;
        boolean hasDigit = false;

        for (char c : password.toCharArray()) {
            if (Character.isLowerCase(c)) {
                hasLower = true;
            }
            if (Character.isUpperCase(c)) {
                hasUpper = true;
            }
            if (Character.isDigit(c)) {
                hasDigit = true;
            }
        }
        return hasLower && hasUpper && hasDigit;
    }
}

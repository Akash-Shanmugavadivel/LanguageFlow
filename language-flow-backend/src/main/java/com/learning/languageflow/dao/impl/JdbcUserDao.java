package com.learning.languageflow.dao.impl;

import com.learning.languageflow.dao.UserDao;
import com.learning.languageflow.dao.mapper.UserRowMapper;
import com.learning.languageflow.model.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcUserDao implements UserDao {

    private final JdbcTemplate jdbcTemplate;
    private final UserRowMapper userRowMapper;

    public JdbcUserDao(JdbcTemplate jdbcTemplate, UserRowMapper userRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.userRowMapper = userRowMapper;
    }

    @Override
    public Optional<User> findById(Integer id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try {
            User user = jdbcTemplate.queryForObject(sql, userRowMapper, id);
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, userRowMapper);
    }

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            String sql = "INSERT INTO users (username, password, streak, daily_goal_minutes, progress_percentage) VALUES (?, ?, ?, ?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, user.getUsername());
                ps.setString(2, user.getPassword());
                ps.setInt(3, user.getStreak());
                ps.setInt(4, user.getDailyGoalMinutes());
                ps.setDouble(5, user.getProgressPercentage());
                return ps;
            }, keyHolder);

            Number key = keyHolder.getKey();
            if (key != null) {
                user.setId(key.intValue());
            }
        } else {
            String sql = "UPDATE users SET username = ?, password = ?, streak = ?, daily_goal_minutes = ?, progress_percentage = ? WHERE id = ?";
            jdbcTemplate.update(sql, user.getUsername(), user.getPassword(), user.getStreak(), user.getDailyGoalMinutes(), user.getProgressPercentage(), user.getId());
        }
        return user;
    }

    @Override
    public boolean deleteById(Integer id) {
        String sql = "DELETE FROM users WHERE id = ?";
        return jdbcTemplate.update(sql, id) > 0;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try {
            User user = jdbcTemplate.queryForObject(sql, userRowMapper, username);
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean existsByUsername(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, username);
        return count != null && count > 0;
    }

    @Override
    public int updateLastLoginDate(String username, LocalDate date) {
        try {
            String sql = "UPDATE users SET last_login_date = ? WHERE username = ?";
            return jdbcTemplate.update(sql, Date.valueOf(date), username);
        } catch (Exception ex) {
            return 0;
        }
    }

    @Override
    public int incrementStreak(String username) {
        String sql = "UPDATE users SET streak = streak + 1 WHERE username = ?";
        return jdbcTemplate.update(sql, username);
    }

    @Override
    public int resetStreak(String username) {
        String sql = "UPDATE users SET streak = 0 WHERE username = ?";
        return jdbcTemplate.update(sql, username);
    }

    @Override
    public int updateStreak(String username, int streak) {
        String sql = "UPDATE users SET streak = ? WHERE username = ?";
        return jdbcTemplate.update(sql, streak, username);
    }

    @Override
    public int updateDailyGoal(String username, int dailyGoalMinutes) {
        String sql = "UPDATE users SET daily_goal_minutes = ? WHERE username = ?";
        return jdbcTemplate.update(sql, dailyGoalMinutes, username);
    }

    @Override
    public int updateProgressPercentage(String username, double progressPercentage) {
        String sql = "UPDATE users SET progress_percentage = ? WHERE username = ?";
        return jdbcTemplate.update(sql, progressPercentage, username);
    }

    @Override
    public int updateProgressAndIncrementStreak(String username, double progressPercentage) {
        String sql = "UPDATE users SET progress_percentage = ?, streak = streak + 1 WHERE username = ?";
        return jdbcTemplate.update(sql, progressPercentage, username);
    }
}

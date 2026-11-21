package com.learning.languageflow.dao.mapper;

import com.learning.languageflow.model.User;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setStreak(rs.getInt("streak"));
        user.setDailyGoalMinutes(rs.getInt("daily_goal_minutes"));
        user.setProgressPercentage(rs.getDouble("progress_percentage"));

        try {
            Date lastLogin = rs.getDate("last_login_date");
            if (lastLogin != null) {
                user.setLastLoginDate(lastLogin.toLocalDate());
            }
        } catch (SQLException ignored) {
            // Field optional in some database states
        }
        return user;
    }
}

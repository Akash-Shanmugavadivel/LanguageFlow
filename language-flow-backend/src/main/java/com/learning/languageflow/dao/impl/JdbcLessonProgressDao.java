package com.learning.languageflow.dao.impl;

import com.learning.languageflow.dao.LessonProgressDao;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JdbcLessonProgressDao implements LessonProgressDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcLessonProgressDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void markLessonCompleted(String username, int lessonId) {
        String sql = "INSERT INTO lesson_progress (username, lesson_id, completed) VALUES (?, ?, 1) " +
                     "ON DUPLICATE KEY UPDATE completed = 1";
        jdbcTemplate.update(sql, username, lessonId);
    }

    @Override
    public int countCompletedByCourseId(String username, int courseId) {
        String sql = "SELECT COUNT(*) FROM lesson_progress " +
                     "WHERE lesson_id IN (SELECT id FROM lessons WHERE course_id = ?) " +
                     "AND username = ? AND completed = 1";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, courseId, username);
        return count != null ? count : 0;
    }

    @Override
    public List<Integer> findCompletedLessonIdsByCourseId(String username, int courseId) {
        String sql = "SELECT lp.lesson_id FROM lesson_progress lp " +
                     "JOIN lessons l ON lp.lesson_id = l.id " +
                     "WHERE l.course_id = ? AND lp.username = ? AND lp.completed = 1";
        return jdbcTemplate.queryForList(sql, Integer.class, courseId, username);
    }

    @Override
    public boolean isLessonCompleted(String username, int lessonId) {
        String sql = "SELECT COUNT(*) FROM lesson_progress WHERE username = ? AND lesson_id = ? AND completed = 1";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, username, lessonId);
        return count != null && count > 0;
    }
}

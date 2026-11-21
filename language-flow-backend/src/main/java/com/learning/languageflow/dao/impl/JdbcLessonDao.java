package com.learning.languageflow.dao.impl;

import com.learning.languageflow.dao.LessonDao;
import com.learning.languageflow.dao.mapper.LessonRowMapper;
import com.learning.languageflow.model.Lesson;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcLessonDao implements LessonDao {

    private final JdbcTemplate jdbcTemplate;
    private final LessonRowMapper lessonRowMapper;

    public JdbcLessonDao(JdbcTemplate jdbcTemplate, LessonRowMapper lessonRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.lessonRowMapper = lessonRowMapper;
    }

    @Override
    public Optional<Lesson> findById(Integer id) {
        String sql = "SELECT * FROM lessons WHERE id = ?";
        try {
            Lesson lesson = jdbcTemplate.queryForObject(sql, lessonRowMapper, id);
            return Optional.ofNullable(lesson);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Lesson> findAll() {
        String sql = "SELECT * FROM lessons ORDER BY id ASC";
        return jdbcTemplate.query(sql, lessonRowMapper);
    }

    @Override
    public Lesson save(Lesson lesson) {
        if (lesson.getId() == null) {
            String sql = "INSERT INTO lessons (course_id, title, content, difficulty_level) VALUES (?, ?, ?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, lesson.getCourseId());
                ps.setString(2, lesson.getTitle());
                ps.setString(3, lesson.getContent());
                ps.setInt(4, lesson.getDifficultyLevelAsInt());
                return ps;
            }, keyHolder);

            Number key = keyHolder.getKey();
            if (key != null) {
                lesson.setId(key.intValue());
            }
        } else {
            String sql = "UPDATE lessons SET course_id = ?, title = ?, content = ?, difficulty_level = ? WHERE id = ?";
            jdbcTemplate.update(sql, lesson.getCourseId(), lesson.getTitle(), lesson.getContent(), lesson.getDifficultyLevelAsInt(), lesson.getId());
        }
        return lesson;
    }

    @Override
    public boolean deleteById(Integer id) {
        String sql = "DELETE FROM lessons WHERE id = ?";
        return jdbcTemplate.update(sql, id) > 0;
    }

    @Override
    public List<Lesson> findByCourseId(int courseId) {
        String sql = "SELECT * FROM lessons WHERE course_id = ? ORDER BY id ASC";
        return jdbcTemplate.query(sql, lessonRowMapper, courseId);
    }

    @Override
    public Optional<Lesson> findByCourseIdAndOffset(int courseId, int offset) {
        String sql = "SELECT * FROM lessons WHERE course_id = ? ORDER BY id ASC LIMIT 1 OFFSET ?";
        try {
            Lesson lesson = jdbcTemplate.queryForObject(sql, lessonRowMapper, courseId, offset);
            return Optional.ofNullable(lesson);
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    @Override
    public int countByCourseId(int courseId) {
        String sql = "SELECT COUNT(*) FROM lessons WHERE course_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, courseId);
        return count != null ? count : 0;
    }
}

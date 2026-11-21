package com.learning.languageflow.dao.impl;

import com.learning.languageflow.dao.CourseDao;
import com.learning.languageflow.dao.mapper.CourseRowMapper;
import com.learning.languageflow.model.Course;
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
public class JdbcCourseDao implements CourseDao {

    private final JdbcTemplate jdbcTemplate;
    private final CourseRowMapper courseRowMapper;

    public JdbcCourseDao(JdbcTemplate jdbcTemplate, CourseRowMapper courseRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.courseRowMapper = courseRowMapper;
    }

    @Override
    public Optional<Course> findById(Integer id) {
        String sql = "SELECT id, name, description FROM courses WHERE id = ?";
        try {
            Course course = jdbcTemplate.queryForObject(sql, courseRowMapper, id);
            return Optional.ofNullable(course);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Course> findAll() {
        String sql = "SELECT id, name, description FROM courses ORDER BY id ASC";
        return jdbcTemplate.query(sql, courseRowMapper);
    }

    @Override
    public Course save(Course course) {
        if (course.getId() == null) {
            String sql = "INSERT INTO courses (name, description) VALUES (?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, course.getName());
                ps.setString(2, course.getDescription());
                return ps;
            }, keyHolder);

            Number key = keyHolder.getKey();
            if (key != null) {
                course.setId(key.intValue());
            }
        } else {
            String sql = "UPDATE courses SET name = ?, description = ? WHERE id = ?";
            jdbcTemplate.update(sql, course.getName(), course.getDescription(), course.getId());
        }
        return course;
    }

    @Override
    public boolean deleteById(Integer id) {
        String sql = "DELETE FROM courses WHERE id = ?";
        return jdbcTemplate.update(sql, id) > 0;
    }

    @Override
    public Optional<Course> findByName(String name) {
        String sql = "SELECT id, name, description FROM courses WHERE name = ?";
        try {
            Course course = jdbcTemplate.queryForObject(sql, courseRowMapper, name);
            return Optional.ofNullable(course);
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }
}

package com.learning.languageflow.dao.impl;

import com.learning.languageflow.dao.QuizDao;
import com.learning.languageflow.dao.mapper.QuizQuestionRowMapper;
import com.learning.languageflow.model.QuizQuestion;
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
public class JdbcQuizDao implements QuizDao {

    private final JdbcTemplate jdbcTemplate;
    private final QuizQuestionRowMapper quizQuestionRowMapper;

    public JdbcQuizDao(JdbcTemplate jdbcTemplate, QuizQuestionRowMapper quizQuestionRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.quizQuestionRowMapper = quizQuestionRowMapper;
    }

    @Override
    public Optional<QuizQuestion> findById(Integer id) {
        String sql = "SELECT * FROM quizzes WHERE id = ?";
        try {
            QuizQuestion q = jdbcTemplate.queryForObject(sql, quizQuestionRowMapper, id);
            return Optional.ofNullable(q);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<QuizQuestion> findAll() {
        String sql = "SELECT * FROM quizzes ORDER BY id ASC";
        return jdbcTemplate.query(sql, quizQuestionRowMapper);
    }

    @Override
    public QuizQuestion save(QuizQuestion question) {
        if (question.getId() == null) {
            String sql = "INSERT INTO quizzes (lesson_id, question, option_a, option_b, option_c, option_d, correct_option) VALUES (?, ?, ?, ?, ?, ?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, question.getLessonId());
                ps.setString(2, question.getQuestion());
                ps.setString(3, question.getOptionA());
                ps.setString(4, question.getOptionB());
                ps.setString(5, question.getOptionC());
                ps.setString(6, question.getOptionD());
                ps.setString(7, String.valueOf(question.getCorrectOption()));
                return ps;
            }, keyHolder);

            Number key = keyHolder.getKey();
            if (key != null) {
                question.setId(key.intValue());
            }
        } else {
            String sql = "UPDATE quizzes SET lesson_id = ?, question = ?, option_a = ?, option_b = ?, option_c = ?, option_d = ?, correct_option = ? WHERE id = ?";
            jdbcTemplate.update(sql, question.getLessonId(), question.getQuestion(), question.getOptionA(), question.getOptionB(),
                    question.getOptionC(), question.getOptionD(), String.valueOf(question.getCorrectOption()), question.getId());
        }
        return question;
    }

    @Override
    public boolean deleteById(Integer id) {
        String sql = "DELETE FROM quizzes WHERE id = ?";
        return jdbcTemplate.update(sql, id) > 0;
    }

    @Override
    public List<QuizQuestion> findByCourseId(int courseId) {
        String sql = "SELECT * FROM quizzes WHERE lesson_id IN (SELECT id FROM lessons WHERE course_id = ?) ORDER BY id ASC";
        return jdbcTemplate.query(sql, quizQuestionRowMapper, courseId);
    }

    @Override
    public List<QuizQuestion> findByLessonId(int lessonId) {
        String sql = "SELECT * FROM quizzes WHERE lesson_id = ? ORDER BY id ASC";
        return jdbcTemplate.query(sql, quizQuestionRowMapper, lessonId);
    }
}

package com.learning.languageflow.dao.mapper;

import com.learning.languageflow.model.QuizQuestion;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class QuizQuestionRowMapper implements RowMapper<QuizQuestion> {

    @Override
    public QuizQuestion mapRow(ResultSet rs, int rowNum) throws SQLException {
        QuizQuestion question = new QuizQuestion();
        question.setId(rs.getInt("id"));
        question.setLessonId(rs.getInt("lesson_id"));
        question.setQuestion(rs.getString("question"));
        question.setOptionA(rs.getString("option_a"));
        question.setOptionB(rs.getString("option_b"));
        question.setOptionC(rs.getString("option_c"));
        question.setOptionD(rs.getString("option_d"));

        String correct = rs.getString("correct_option");
        question.setCorrectOption(correct != null && !correct.isEmpty() ? correct.charAt(0) : 'A');
        return question;
    }
}

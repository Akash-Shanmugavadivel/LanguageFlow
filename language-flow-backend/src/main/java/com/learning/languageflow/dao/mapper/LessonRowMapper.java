package com.learning.languageflow.dao.mapper;

import com.learning.languageflow.model.Lesson;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class LessonRowMapper implements RowMapper<Lesson> {

    @Override
    public Lesson mapRow(ResultSet rs, int rowNum) throws SQLException {
        Lesson lesson = new Lesson();
        lesson.setId(rs.getInt("id"));
        lesson.setCourseId(rs.getInt("course_id"));
        lesson.setTitle(rs.getString("title"));
        lesson.setContent(rs.getString("content"));
        lesson.setDifficultyLevelFromInt(rs.getInt("difficulty_level"));
        return lesson;
    }
}

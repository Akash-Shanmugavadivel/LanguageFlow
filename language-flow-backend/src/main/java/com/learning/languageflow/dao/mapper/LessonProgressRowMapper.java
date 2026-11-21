package com.learning.languageflow.dao.mapper;

import com.learning.languageflow.model.LessonProgress;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class LessonProgressRowMapper implements RowMapper<LessonProgress> {

    @Override
    public LessonProgress mapRow(ResultSet rs, int rowNum) throws SQLException {
        LessonProgress lp = new LessonProgress();
        lp.setId(rs.getInt("id"));
        lp.setUsername(rs.getString("username"));
        lp.setLessonId(rs.getInt("lesson_id"));
        lp.setCompleted(rs.getBoolean("completed"));
        return lp;
    }
}

package com.learning.languageflow.dao;

import com.learning.languageflow.model.QuizQuestion;

import java.util.List;

/**
 * Contract for Quiz persistence operations.
 */
public interface QuizDao extends GenericDao<QuizQuestion, Integer> {

    List<QuizQuestion> findByCourseId(int courseId);

    List<QuizQuestion> findByLessonId(int lessonId);
}

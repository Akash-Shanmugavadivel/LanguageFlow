package com.learning.languageflow.dao;

import com.learning.languageflow.model.Lesson;

import java.util.List;
import java.util.Optional;

/**
 * Contract for Lesson persistence operations.
 */
public interface LessonDao extends GenericDao<Lesson, Integer> {

    List<Lesson> findByCourseId(int courseId);

    Optional<Lesson> findByCourseIdAndOffset(int courseId, int offset);

    int countByCourseId(int courseId);
}

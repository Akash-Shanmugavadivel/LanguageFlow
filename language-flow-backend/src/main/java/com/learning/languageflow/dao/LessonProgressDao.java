package com.learning.languageflow.dao;

import java.util.List;

/**
 * Contract for user Lesson Progress persistence operations.
 */
public interface LessonProgressDao {

    void markLessonCompleted(String username, int lessonId);

    int countCompletedByCourseId(String username, int courseId);

    List<Integer> findCompletedLessonIdsByCourseId(String username, int courseId);

    boolean isLessonCompleted(String username, int lessonId);
}

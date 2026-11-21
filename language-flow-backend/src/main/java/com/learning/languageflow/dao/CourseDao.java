package com.learning.languageflow.dao;

import com.learning.languageflow.model.Course;

import java.util.Optional;

/**
 * Contract for Course persistence operations.
 */
public interface CourseDao extends GenericDao<Course, Integer> {

    Optional<Course> findByName(String name);
}

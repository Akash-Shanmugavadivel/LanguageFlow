package com.learning.languageflow.service;

import com.learning.languageflow.dao.CourseDao;
import com.learning.languageflow.model.Course;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    private final CourseDao courseDao;

    public CourseService(CourseDao courseDao) {
        this.courseDao = courseDao;
    }

    public List<Course> getAllCourses() {
        return courseDao.findAll();
    }

    public Course getCourseById(int id) {
        return courseDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Course not found with id: " + id));
    }

    public int getCourseIdByName(String name) {
        return courseDao.findByName(name)
                .map(Course::getId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found with name: " + name));
    }

    public Course createCourse(Course course) {
        if (course.getName() == null || course.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Course name cannot be empty.");
        }
        return courseDao.save(course);
    }

    public Course updateCourse(int id, Course updatedCourse) {
        Course existing = getCourseById(id);
        if (updatedCourse.getName() != null && !updatedCourse.getName().trim().isEmpty()) {
            existing.setName(updatedCourse.getName().trim());
        }
        if (updatedCourse.getDescription() != null) {
            existing.setDescription(updatedCourse.getDescription());
        }
        return courseDao.save(existing);
    }

    public boolean deleteCourse(int id) {
        return courseDao.deleteById(id);
    }
}

package com.learning.languageflow.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Domain Aggregate Root representing a language curriculum.
 */
public class Course extends BaseEntity {

    private String name;
    private String description;
    private final List<Lesson> lessons = new ArrayList<>();

    public Course() {
        super();
    }

    public Course(Integer id, String name, String description) {
        super(id);
        this.name = name;
        this.description = description;
    }

    public Course(String name, String description) {
        super(null);
        this.name = name;
        this.description = description;
    }

    // --- Domain Behaviors ---

    public void addLesson(Lesson lesson) {
        if (lesson != null) {
            if (this.id != null) {
                lesson.setCourseId(this.id);
            }
            this.lessons.add(lesson);
        }
    }

    public List<Lesson> getLessons() {
        return Collections.unmodifiableList(lessons);
    }

    public int getTotalLessons() {
        return lessons.size();
    }

    // --- Getters and Setters ---

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // --- Builder Pattern ---

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer id;
        private String name;
        private String description;
        private List<Lesson> lessons = new ArrayList<>();

        public Builder id(Integer id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder lessons(List<Lesson> lessons) {
            if (lessons != null) {
                this.lessons = lessons;
            }
            return this;
        }

        public Course build() {
            Course course = new Course(id, name, description);
            this.lessons.forEach(course::addLesson);
            return course;
        }
    }
}

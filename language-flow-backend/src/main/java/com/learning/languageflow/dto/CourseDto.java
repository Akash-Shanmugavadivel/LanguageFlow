package com.learning.languageflow.dto;

public class CourseDto {

    private Integer id;
    private String name;
    private String description;
    private int totalLessons;

    public CourseDto() {
    }

    public CourseDto(Integer id, String name, String description, int totalLessons) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.totalLessons = totalLessons;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public int getTotalLessons() {
        return totalLessons;
    }

    public void setTotalLessons(int totalLessons) {
        this.totalLessons = totalLessons;
    }
}

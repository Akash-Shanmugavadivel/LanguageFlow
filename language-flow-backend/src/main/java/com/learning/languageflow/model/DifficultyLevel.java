package com.learning.languageflow.model;

/**
 * Domain Enum representing course and lesson difficulty levels.
 */
public enum DifficultyLevel {
    BEGINNER(1, "Culture / Intro"),
    BASIC(2, "Basic"),
    INTERMEDIATE(3, "Intermediate"),
    ADVANCED(4, "Advanced"),
    CONVERSATION(5, "Conversation / Mastery");

    private final int level;
    private final String description;

    DifficultyLevel(int level, String description) {
        this.level = level;
        this.description = description;
    }

    public int getLevel() {
        return level;
    }

    public String getDescription() {
        return description;
    }

    public static DifficultyLevel fromLevel(int level) {
        for (DifficultyLevel dl : values()) {
            if (dl.level == level) {
                return dl;
            }
        }
        return BASIC;
    }
}

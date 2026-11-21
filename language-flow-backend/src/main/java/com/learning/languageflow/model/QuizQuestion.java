package com.learning.languageflow.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Domain Entity representing an interactive multiple-choice assessment question.
 */
public class QuizQuestion extends BaseEntity {

    private Integer lessonId;
    private String question;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private char correctOption;

    public QuizQuestion() {
        super();
        this.correctOption = 'A';
    }

    public QuizQuestion(Integer id, Integer lessonId, String question, String optionA, String optionB, String optionC, String optionD, char correctOption) {
        super(id);
        this.lessonId = lessonId;
        this.question = question;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correctOption = Character.toUpperCase(correctOption);
    }

    public QuizQuestion(String question, String optionA, String optionB, String optionC, String optionD, char correctOption) {
        this(null, null, question, optionA, optionB, optionC, optionD, correctOption);
    }

    // --- Domain Behaviors ---

    /**
     * Domain evaluation: checks whether the provided answer key matches the correct option.
     */
    public boolean isCorrectAnswer(Character selectedOption) {
        if (selectedOption == null) {
            return false;
        }
        return Character.toUpperCase(selectedOption) == this.correctOption;
    }

    /**
     * Returns an unmodifiable map of options: Key ('A','B','C','D') -> Value (Text).
     */
    public Map<Character, String> getOptionsMap() {
        Map<Character, String> map = new LinkedHashMap<>();
        map.put('A', optionA);
        map.put('B', optionB);
        map.put('C', optionC);
        map.put('D', optionD);
        return map;
    }

    // --- Getters and Setters ---

    public Integer getLessonId() {
        return lessonId;
    }

    public void setLessonId(Integer lessonId) {
        this.lessonId = lessonId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOptionA() {
        return optionA;
    }

    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public void setOptionD(String optionD) {
        this.optionD = optionD;
    }

    public char getCorrectOption() {
        return correctOption;
    }

    public void setCorrectOption(char correctOption) {
        this.correctOption = Character.toUpperCase(correctOption);
    }

    // --- Builder Pattern ---

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer id;
        private Integer lessonId;
        private String question;
        private String optionA;
        private String optionB;
        private String optionC;
        private String optionD;
        private char correctOption = 'A';

        public Builder id(Integer id) {
            this.id = id;
            return this;
        }

        public Builder lessonId(Integer lessonId) {
            this.lessonId = lessonId;
            return this;
        }

        public Builder question(String question) {
            this.question = question;
            return this;
        }

        public Builder optionA(String optionA) {
            this.optionA = optionA;
            return this;
        }

        public Builder optionB(String optionB) {
            this.optionB = optionB;
            return this;
        }

        public Builder optionC(String optionC) {
            this.optionC = optionC;
            return this;
        }

        public Builder optionD(String optionD) {
            this.optionD = optionD;
            return this;
        }

        public Builder correctOption(char correctOption) {
            this.correctOption = Character.toUpperCase(correctOption);
            return this;
        }

        public QuizQuestion build() {
            return new QuizQuestion(id, lessonId, question, optionA, optionB, optionC, optionD, correctOption);
        }
    }
}

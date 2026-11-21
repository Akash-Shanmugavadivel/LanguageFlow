package com.learning.languageflow.dto;

public class QuizQuestionAdminDto extends QuizQuestionDto {

    private char correctOption;

    public QuizQuestionAdminDto() {
        super();
    }

    public QuizQuestionAdminDto(Integer id, Integer lessonId, String question, String optionA, String optionB, String optionC, String optionD, char correctOption) {
        super(id, lessonId, question, optionA, optionB, optionC, optionD);
        this.correctOption = correctOption;
    }

    public char getCorrectOption() {
        return correctOption;
    }

    public void setCorrectOption(char correctOption) {
        this.correctOption = correctOption;
    }
}

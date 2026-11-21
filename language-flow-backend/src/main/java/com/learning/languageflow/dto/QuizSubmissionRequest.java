package com.learning.languageflow.dto;

import java.util.Map;

public class QuizSubmissionRequest {

    private String username;
    private Map<Integer, Character> answers; // questionId -> option ('A', 'B', 'C', 'D')

    public QuizSubmissionRequest() {
    }

    public QuizSubmissionRequest(String username, Map<Integer, Character> answers) {
        this.username = username;
        this.answers = answers;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Map<Integer, Character> getAnswers() {
        return answers;
    }

    public void setAnswers(Map<Integer, Character> answers) {
        this.answers = answers;
    }
}

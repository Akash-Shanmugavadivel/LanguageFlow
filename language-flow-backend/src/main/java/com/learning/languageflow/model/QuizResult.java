package com.learning.languageflow.model;

/**
 * Value Object representing the outcome and evaluation of a submitted quiz.
 */
public class QuizResult {

    private int score;
    private int totalQuestions;
    private double quizPercentage;
    private double newOverallProgressPercentage;
    private boolean passed;
    private String feedbackMessage;

    public QuizResult() {
    }

    public QuizResult(int score, int totalQuestions, double quizPercentage, double newOverallProgressPercentage) {
        this.score = score;
        this.totalQuestions = totalQuestions;
        this.quizPercentage = quizPercentage;
        this.newOverallProgressPercentage = newOverallProgressPercentage;
        this.passed = quizPercentage >= 60.0;
        this.feedbackMessage = generateFeedback(quizPercentage);
    }

    private String generateFeedback(double percentage) {
        if (percentage >= 90.0) {
            return "Outstanding! You mastered this topic!";
        } else if (percentage >= 75.0) {
            return "Great job! Strong comprehension demonstrated.";
        } else if (percentage >= 60.0) {
            return "Passed! Keep practicing to strengthen your skills.";
        } else {
            return "Needs review. Revisit the lesson content and try again!";
        }
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public double getQuizPercentage() {
        return quizPercentage;
    }

    public void setQuizPercentage(double quizPercentage) {
        this.quizPercentage = quizPercentage;
    }

    public double getNewOverallProgressPercentage() {
        return newOverallProgressPercentage;
    }

    public void setNewOverallProgressPercentage(double newOverallProgressPercentage) {
        this.newOverallProgressPercentage = newOverallProgressPercentage;
    }

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    public String getFeedbackMessage() {
        return feedbackMessage;
    }

    public void setFeedbackMessage(String feedbackMessage) {
        this.feedbackMessage = feedbackMessage;
    }
}

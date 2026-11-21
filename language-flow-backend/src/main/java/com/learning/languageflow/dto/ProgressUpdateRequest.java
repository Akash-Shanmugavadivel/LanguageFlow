package com.learning.languageflow.dto;

public class ProgressUpdateRequest {

    private double progressPercentage;

    public ProgressUpdateRequest() {
    }

    public ProgressUpdateRequest(double progressPercentage) {
        this.progressPercentage = progressPercentage;
    }

    public double getProgressPercentage() {
        return progressPercentage;
    }

    public void setProgressPercentage(double progressPercentage) {
        this.progressPercentage = progressPercentage;
    }
}

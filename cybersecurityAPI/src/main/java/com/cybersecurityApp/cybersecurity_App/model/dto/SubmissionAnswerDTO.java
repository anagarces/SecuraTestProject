package com.cybersecurityApp.cybersecurity_App.model.dto;

public class SubmissionAnswerDTO {

    private Long questionId;
    private Long optionId;

    // Getters y setters
    public Long getQuestionId() { return questionId; }
    public void setQuestionId(Long questionId) { this.questionId = questionId; }

    public Long getOptionId() { return optionId; }
    public void setOptionId(Long optionId) { this.optionId = optionId; }
}


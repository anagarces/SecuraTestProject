package com.cybersecurityApp.cybersecurity_App.model.dto;

import java.util.List;

public class SubmissionRequestDTO {

    private Long userId;
    private Long quizId;
    private List<SubmissionAnswerDTO> answers;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getQuizId() {
        return quizId;
    }

    public void setQuizId(Long quizId) {
        this.quizId = quizId;
    }

    public List<SubmissionAnswerDTO> getAnswers() {
        return answers;
    }

    public void setAnswers(List<SubmissionAnswerDTO> answers) {
        this.answers = answers;
    }
}

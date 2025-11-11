package com.cybersecurityApp.cybersecurity_App.model.dto;

import java.time.LocalDateTime;

public class SubmissionSummaryDTO {

    private Long id;             // id de la submission
    private Long quizId;         // id del quiz
    private String quizTitle;    // título del quiz
    private int score;           // puntuación obtenida
    private int totalQuestions;  // total de preguntas
    private LocalDateTime submittedAt;
    private LocalDateTime finishedAt;

    public SubmissionSummaryDTO() {}

    public SubmissionSummaryDTO(Long id, Long quizId, String quizTitle, int score, int totalQuestions,
                                LocalDateTime submittedAt, LocalDateTime finishedAt) {
        this.id = id;
        this.quizId = quizId;
        this.quizTitle = quizTitle;
        this.score = score;
        this.totalQuestions = totalQuestions;
        this.submittedAt = submittedAt;
        this.finishedAt = finishedAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getQuizId() { return quizId; }
    public void setQuizId(Long quizId) { this.quizId = quizId; }

    public String getQuizTitle() { return quizTitle; }
    public void setQuizTitle(String quizTitle) { this.quizTitle = quizTitle; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public int getTotalQuestions() { return totalQuestions; }
    public void setTotalQuestions(int totalQuestions) { this.totalQuestions = totalQuestions; }

    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }

    public LocalDateTime getFinishedAt() { return finishedAt; }
    public void setFinishedAt(LocalDateTime finishedAt) { this.finishedAt = finishedAt; }
}

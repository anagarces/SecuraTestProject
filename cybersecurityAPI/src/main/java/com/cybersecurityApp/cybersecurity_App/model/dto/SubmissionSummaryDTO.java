package com.cybersecurityApp.cybersecurity_App.model.dto;

import java.time.LocalDateTime;

public class SubmissionSummaryDTO {

    private Long id;
    private Long quizId;
    private String quizTitle;
    private String userEmail;
    private String userName;
    private int score;
    private int totalQuestions;
    private LocalDateTime submittedAt;
    private LocalDateTime finishedAt;

    public SubmissionSummaryDTO() {}

    // 2. EL CONSTRUCTOR AHORA RECIBE EL EMAIL (4to parámetro)
    public SubmissionSummaryDTO(Long id, Long quizId, String quizTitle, String userEmail, String userName, int score, int totalQuestions,
                                LocalDateTime submittedAt, LocalDateTime finishedAt) {
        this.id = id;
        this.quizId = quizId;
        this.quizTitle = quizTitle;
        this.userEmail = userEmail;
        this.userName = userName;
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

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public int getTotalQuestions() { return totalQuestions; }
    public void setTotalQuestions(int totalQuestions) { this.totalQuestions = totalQuestions; }

    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }

    public LocalDateTime getFinishedAt() { return finishedAt; }
    public void setFinishedAt(LocalDateTime finishedAt) { this.finishedAt = finishedAt; }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
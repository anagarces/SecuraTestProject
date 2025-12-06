package com.cybersecurityApp.cybersecurity_App.model.dto;

import java.util.List;

public class QuizDetailDTO {

    private Long id;
    private String title;
    private String description;
    private List<QuestionStudentDTO> questions;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<QuestionStudentDTO> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionStudentDTO> questions) {
        this.questions = questions;
    }
}

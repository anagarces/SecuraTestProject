package com.cybersecurityApp.cybersecurity_App.model.dto;

import java.util.List;

public class QuestionStudentDTO {
    private Long id;
    private String text;
    
    private List<OptionItemStudentDTO> options;

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public List<OptionItemStudentDTO> getOptions() { return options; }
    public void setOptions(List<OptionItemStudentDTO> options) { this.options = options; }
}

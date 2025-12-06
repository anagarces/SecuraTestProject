package com.cybersecurityApp.cybersecurity_App.model.dto.adminUser;

import java.util.List;

public class QuestionAdminDTO {
    private Long id;
    private String text;
    // Usa la lista con respuestas
    private List<OptionItemAdminDTO> options;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<OptionItemAdminDTO> getOptions() {
        return options;
    }

    public void setOptions(List<OptionItemAdminDTO> options) {
        this.options = options;
    }
}
package com.cybersecurityApp.cybersecurity_App.model.dto;

import java.util.List;

public class QuestionDTO {

    private Long id;
    private String text;
    private List<OptionItemDTO> options;

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

    public List<OptionItemDTO> getOptions() {
        return options;
    }

    public void setOptions(List<OptionItemDTO> options) {
        this.options = options;
    }
}

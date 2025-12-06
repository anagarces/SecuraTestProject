package com.cybersecurityApp.cybersecurity_App.model.dto;

//SOLO lleva lo que el estudiante necesita ver.

public class OptionItemStudentDTO {

    private Long id;
    private String text;

    public OptionItemStudentDTO() {
    }

    public OptionItemStudentDTO(Long id, String text) {
        this.id = id;
        this.text = text;
    }

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
}
package com.cybersecurityApp.cybersecurity_App.model.dto.adminUser;

import com.cybersecurityApp.cybersecurity_App.model.dto.OptionItemStudentDTO;

public class OptionItemAdminDTO extends OptionItemStudentDTO {

    private boolean correct;

    public OptionItemAdminDTO() {
        super();
    }

    public OptionItemAdminDTO(Long id, String text, boolean correct) {
        super(id, text);
        this.correct = correct;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }
}
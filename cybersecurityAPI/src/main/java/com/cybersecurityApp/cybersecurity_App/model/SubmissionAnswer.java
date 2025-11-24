package com.cybersecurityApp.cybersecurity_App.model;

import jakarta.persistence.*;

@Entity
@Table(name = "submission_answer")
public class SubmissionAnswer { //respuestas marcadas por el usuario en un envio, una fila por pregunta
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "submission_id")
    private Submission submission;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = true)
    private Question question;

    @ManyToOne
    @JoinColumn(name = "selected_option_id", nullable = true)
    private OptionItem option;


    public SubmissionAnswer(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Submission getSubmission() {
        return submission;
    }

    public void setSubmission(Submission submission) {
        this.submission = submission;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public OptionItem getOption() {
        return option;
    }

    public void setOption(OptionItem option) {
        this.option = option;
    }
}
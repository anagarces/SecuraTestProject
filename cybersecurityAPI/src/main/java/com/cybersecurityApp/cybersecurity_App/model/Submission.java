package com.cybersecurityApp.cybersecurity_App.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "submission")
public class Submission { // envio del quiz terminado de un usuario

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name = "user_id")
    private Usuario user;

    @ManyToOne @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    @OneToMany(mappedBy = "submission", cascade = CascadeType.ALL)
    private List<SubmissionAnswer> answers = new ArrayList<>();

    private int score;
    private int totalQuestions;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUser() {
        return user;
    }

    public void setUser(Usuario user) {
        this.user = user;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public List<SubmissionAnswer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<SubmissionAnswer> answers) {
        this.answers = answers;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }
}
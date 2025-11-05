package com.cybersecurityApp.cybersecurity_App.service;

import com.cybersecurityApp.cybersecurity_App.model.*;
import com.cybersecurityApp.cybersecurity_App.model.dao.OptionItemDao;
import com.cybersecurityApp.cybersecurity_App.model.dao.QuizDao;
import com.cybersecurityApp.cybersecurity_App.model.dao.SubmissionDao;
import com.cybersecurityApp.cybersecurity_App.model.dao.UserDao;
import com.cybersecurityApp.cybersecurity_App.model.dto.SubmissionAnswerDTO;
import com.cybersecurityApp.cybersecurity_App.model.dto.SubmissionRequestDTO;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubmissionService {


    private final SubmissionDao submissionDao;
    private final OptionItemDao optionItemDao;
    private final QuizDao quizDao;
    private final UserDao userDao;

    public SubmissionService(SubmissionDao submissionDao, OptionItemDao optionItemDao,
                             QuizDao quizDao, UserDao userDao) {
        this.submissionDao = submissionDao;
        this.optionItemDao = optionItemDao;
        this.quizDao = quizDao;
        this.userDao = userDao;
    }

    @Transactional
    public Submission processSubmission(SubmissionRequestDTO dto) {
        Submission submission = new Submission();
        submission.setQuiz(quizDao.findById(dto.getQuizId()).orElseThrow());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            throw new IllegalStateException("No se pudo determinar el usuario autenticado");
        }

        String email = authentication.getName();
        Usuario user = userDao.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Usuario no encontrado: " + email));
        submission.setUser(user);

        int score = 0;

        for (SubmissionAnswerDTO answer : dto.getAnswers()) {
            OptionItem selected = optionItemDao.findById(answer.getOptionId()).orElseThrow();

            SubmissionAnswer subAnswer = new SubmissionAnswer();
            subAnswer.setQuestion(selected.getQuestion());
            subAnswer.setOption(selected);
            subAnswer.setSubmission(submission);

            submission.getAnswers().add(subAnswer);

            if (selected.isCorrect()) {
                score++;
            }
        }

        submission.setScore(score);
        submission.setTotalQuestions(dto.getAnswers().size());

        submissionDao.save(submission);
        return submission;
    }
}

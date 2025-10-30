package com.cybersecurityApp.cybersecurity_App.service;

import com.cybersecurityApp.cybersecurity_App.model.*;
import com.cybersecurityApp.cybersecurity_App.model.dao.OptionItemDao;
import com.cybersecurityApp.cybersecurity_App.model.dao.QuizDao;
import com.cybersecurityApp.cybersecurity_App.model.dao.SubmissionDao;
import com.cybersecurityApp.cybersecurity_App.model.dao.UserDao;
import com.cybersecurityApp.cybersecurity_App.model.dto.SubmissionAnswerDTO;
import com.cybersecurityApp.cybersecurity_App.model.dto.SubmissionRequestDTO;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        submission.setUser(userDao.findById(dto.getUserId()).orElseThrow());

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

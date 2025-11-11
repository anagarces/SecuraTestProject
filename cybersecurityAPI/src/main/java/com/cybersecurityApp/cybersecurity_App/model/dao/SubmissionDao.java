package com.cybersecurityApp.cybersecurity_App.model.dao;

import com.cybersecurityApp.cybersecurity_App.model.Submission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubmissionDao extends JpaRepository<Submission, Long> {

    List<Submission> findByUserId(Long userId);
    List<Submission> findByQuizId(Long quizId);
    List<Submission> findByUserIdOrderBySubmittedAtDesc(Long userId);

}

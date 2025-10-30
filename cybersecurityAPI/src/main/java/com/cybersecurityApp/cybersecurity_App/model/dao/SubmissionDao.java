package com.cybersecurityApp.cybersecurity_App.model.dao;

import com.cybersecurityApp.cybersecurity_App.model.Submission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubmissionDao extends JpaRepository<Submission, Long> {
}

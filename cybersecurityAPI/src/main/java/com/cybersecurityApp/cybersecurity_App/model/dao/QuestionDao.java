package com.cybersecurityApp.cybersecurity_App.model.dao;

import com.cybersecurityApp.cybersecurity_App.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionDao extends JpaRepository<Question, Long> {
}

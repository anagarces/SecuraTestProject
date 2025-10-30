package com.cybersecurityApp.cybersecurity_App.model.dao;

import com.cybersecurityApp.cybersecurity_App.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;


public interface QuizDao extends JpaRepository<Quiz, Long> {

}

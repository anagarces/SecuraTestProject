package com.cybersecurityApp.cybersecurity_App.controller;

import com.cybersecurityApp.cybersecurity_App.model.Quiz;
import com.cybersecurityApp.cybersecurity_App.model.dao.QuizDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quizzes")
@CrossOrigin(origins = "http://localhost:4200")
public class QuizController {

    @Autowired
    private QuizDao quizDao;

    @GetMapping
    public List<Quiz> getAll() {
        return quizDao.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Quiz> getById(@PathVariable Long id) {
        return quizDao.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Quiz create(@RequestBody Quiz quiz) {
        return quizDao.save(quiz);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Quiz> update(@PathVariable Long id, @RequestBody Quiz quizData) {
        return quizDao.findById(id)
                .map(quiz -> {
                    quiz.setTitle(quizData.getTitle());
                    quiz.setDescription(quizData.getDescription());
                    return ResponseEntity.ok(quizDao.save(quiz));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        quizDao.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
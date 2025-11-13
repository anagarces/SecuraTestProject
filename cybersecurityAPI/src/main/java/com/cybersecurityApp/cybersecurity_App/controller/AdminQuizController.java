package com.cybersecurityApp.cybersecurity_App.controller;

import com.cybersecurityApp.cybersecurity_App.model.*;
import com.cybersecurityApp.cybersecurity_App.model.dao.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/quizzes")
@CrossOrigin(origins = "http://localhost:4200")
@PreAuthorize("hasRole('ADMIN')")
public class AdminQuizController {

    private final QuizDao quizDao;
    private final QuestionDao questionDao;
    private final OptionItemDao optionItemDao;

    public AdminQuizController(QuizDao quizDao, QuestionDao questionDao, OptionItemDao optionItemDao) {
        this.quizDao = quizDao;
        this.questionDao = questionDao;
        this.optionItemDao = optionItemDao;
    }

    // =======================
    // CRUD de QUIZZES
    // =======================

    @PostMapping
    public Quiz createQuiz(@RequestBody Quiz quiz) {
        return quizDao.save(quiz);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Quiz> updateQuiz(@PathVariable Long id, @RequestBody Quiz data) {
        return quizDao.findById(id)
                .map(q -> {
                    q.setTitle(data.getTitle());
                    q.setDescription(data.getDescription());
                    return ResponseEntity.ok(quizDao.save(q));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuiz(@PathVariable Long id) {
        quizDao.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // =======================
    // CRUD de PREGUNTAS
    // =======================

    @PostMapping("/{quizId}/questions")
    public Question createQuestion(@PathVariable Long quizId, @RequestBody Question question) {
        Quiz quiz = quizDao.findById(quizId).orElseThrow();
        question.setQuiz(quiz);
        return questionDao.save(question);
    }

    @PutMapping("/questions/{questionId}")
    public ResponseEntity<Question> updateQuestion(@PathVariable Long questionId, @RequestBody Question data) {
        return questionDao.findById(questionId)
                .map(q -> {
                    q.setText(data.getText());
                    return ResponseEntity.ok(questionDao.save(q));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/questions/{questionId}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long questionId) {
        questionDao.deleteById(questionId);
        return ResponseEntity.noContent().build();
    }

    // =======================
    // CRUD de OPCIONES
    // =======================

    @PostMapping("/questions/{questionId}/options")
    public OptionItem createOption(@PathVariable Long questionId, @RequestBody OptionItem option) {
        Question question = questionDao.findById(questionId).orElseThrow();
        option.setQuestion(question);
        option.setCorrect(option.isCorrect()); //
        return optionItemDao.save(option);
    }

    @PutMapping("/options/{optionId}")
    public ResponseEntity<OptionItem> updateOption(@PathVariable Long optionId, @RequestBody OptionItem data) {
        return optionItemDao.findById(optionId)
                .map(opt -> {
                    opt.setText(data.getText());
                    opt.setCorrect(data.isCorrect());
                    return ResponseEntity.ok(optionItemDao.save(opt));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/options/{optionId}")
    public ResponseEntity<Void> deleteOption(@PathVariable Long optionId) {
        optionItemDao.deleteById(optionId);
        return ResponseEntity.noContent().build();
    }
}

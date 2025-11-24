package com.cybersecurityApp.cybersecurity_App.controller;

import com.cybersecurityApp.cybersecurity_App.model.*;
import com.cybersecurityApp.cybersecurity_App.model.dao.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

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

        // Establecer la relación padre-hijo antes de guardar
        if (quiz.getQuestions() != null) {
            for (Question q : quiz.getQuestions()) {
                q.setQuiz(quiz);

                if (q.getOptions() != null) {
                    for (OptionItem opt : q.getOptions()) {
                        opt.setQuestion(q);
                        // al crear nuevas opciones siempre van activas
                        opt.setArchived(false);
                    }
                }
            }
        }

        return quizDao.save(quiz);
    }

    /**
     * Devuelve un cuestionario concreto para el panel de administración.
     * Oculta las opciones archivadas para simplificar la edición.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Quiz> getQuizById(@PathVariable Long id) {
        return quizDao.findById(id)
                .map(quiz -> {
                    limpiarOpcionesArchivadas(quiz);
                    return ResponseEntity.ok(quiz);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Listado de cuestionarios para el panel de administración.
     * También filtra las opciones archivadas antes de serializar.
     */
    @GetMapping
    public List<Quiz> getAllQuizzes() {
        List<Quiz> quizzes = quizDao.findAll();
        quizzes.forEach(this::limpiarOpcionesArchivadas);
        return quizzes;
    }

    @PutMapping("/{id}")
    public ResponseEntity<Quiz> updateQuiz(@PathVariable Long id, @RequestBody Quiz data) {

        return quizDao.findById(id).map(existingQuiz -> {

            existingQuiz.setTitle(data.getTitle());
            existingQuiz.setDescription(data.getDescription());

            // LIMPIAR TODAS LAS PREGUNTAS ANTERIORES
            existingQuiz.getQuestions().clear();

            // RECONSTRUIR TODAS LAS NUEVAS
            for (Question incomingQuestion : data.getQuestions()) {

                Question newQuestion = new Question();
                newQuestion.setText(incomingQuestion.getText());
                newQuestion.setQuiz(existingQuiz);

                // OPCIONES → LIMPIAR Y RECONSTRUIR
                List<OptionItem> newOptions = new ArrayList<>();

                for (OptionItem incomingOption : incomingQuestion.getOptions()) {
                    OptionItem opt = new OptionItem();
                    opt.setText(incomingOption.getText());
                    opt.setCorrect(incomingOption.isCorrect());
                    opt.setQuestion(newQuestion);

                    newOptions.add(opt);
                }

                newQuestion.setOptions(newOptions);

                existingQuiz.getQuestions().add(newQuestion);
            }

            Quiz saved = quizDao.save(existingQuiz);
            return ResponseEntity.ok(saved);

        }).orElse(ResponseEntity.notFound().build());
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuiz(@PathVariable Long id) {
        quizDao.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // =======================
    // CRUD de PREGUNTAS
    // (se mantienen igual,
    //  pero ahora casi todo se hace vía updateQuiz)
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
    // (si las necesitas puntualmente)
    // =======================

    @PostMapping("/questions/{questionId}/options")
    public OptionItem createOption(@PathVariable Long questionId, @RequestBody OptionItem option) {
        Question question = questionDao.findById(questionId).orElseThrow();
        option.setQuestion(question);
        option.setCorrect(option.isCorrect());
        option.setArchived(false);
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

    @GetMapping("/debug")
    public String debugAuth(org.springframework.security.core.Authentication auth) {
        System.out.println("AUTH = " + auth);
        System.out.println("AUTHORITIES = " + auth.getAuthorities());
        return "OK";
    }

    // =======================
    // Utilidad privada
    // =======================

    /**
     * Elimina de las colecciones en memoria las opciones archivadas,
     * para que el JSON enviado al front solo contenga las activas.
     * No borra nada de la base de datos.
     */
    private void limpiarOpcionesArchivadas(Quiz quiz) {
        if (quiz.getQuestions() == null) return;

        for (Question q : quiz.getQuestions()) {
            if (q.getOptions() == null) continue;
            List<OptionItem> activas = q.getOptions()
                    .stream()
                    .filter(opt -> !opt.isArchived())
                    .collect(Collectors.toList());
            q.setOptions(activas);
        }
    }
}

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
        return quizDao.findById(id)
                .map(existingQuiz -> {

                    // 1) Actualizar datos básicos
                    existingQuiz.setTitle(data.getTitle());
                    existingQuiz.setDescription(data.getDescription());

                    // 2) Mapa de preguntas existentes (por id)
                    Map<Long, Question> existingQuestionsById = existingQuiz.getQuestions()
                            .stream()
                            .filter(q -> q.getId() != null)
                            .collect(Collectors.toMap(Question::getId, q -> q));

                    // 3) Nueva lista de preguntas que quedarán asociadas al quiz
                    List<Question> updatedQuestions = new ArrayList<>();

                    if (data.getQuestions() != null) {
                        for (Question incomingQ : data.getQuestions()) {

                            Question targetQ;

                            // ¿Es una pregunta nueva o ya existe?
                            if (incomingQ.getId() != null && existingQuestionsById.containsKey(incomingQ.getId())) {
                                // Pregunta existente: actualizamos texto
                                targetQ = existingQuestionsById.get(incomingQ.getId());
                                targetQ.setText(incomingQ.getText());
                            } else {
                                // Pregunta nueva
                                targetQ = new Question();
                                targetQ.setText(incomingQ.getText());
                                targetQ.setQuiz(existingQuiz);
                            }

                            // ===== Opciones =====
                            Map<Long, OptionItem> existingOptionsById = targetQ.getOptions()
                                    .stream()
                                    .filter(o -> o.getId() != null)
                                    .collect(Collectors.toMap(OptionItem::getId, o -> o));

                            // Marcaremos cuáles se han tocado para luego archivar las sobras
                            Set<Long> touchedOptionIds = new HashSet<>();

                            if (incomingQ.getOptions() != null) {
                                for (OptionItem incomingOpt : incomingQ.getOptions()) {

                                    OptionItem targetOpt;
                                    if (incomingOpt.getId() != null && existingOptionsById.containsKey(incomingOpt.getId())) {
                                        // Opción existente: actualizar texto y bandera de correcta
                                        targetOpt = existingOptionsById.get(incomingOpt.getId());
                                    } else {
                                        // Opción nueva
                                        targetOpt = new OptionItem();
                                        targetOpt.setQuestion(targetQ);
                                        targetQ.getOptions().add(targetOpt);
                                    }

                                    targetOpt.setText(incomingOpt.getText());
                                    targetOpt.setCorrect(incomingOpt.isCorrect());
                                    // aseguramos que siga activa
                                    targetOpt.setArchived(false);

                                    if (targetOpt.getId() != null) {
                                        touchedOptionIds.add(targetOpt.getId());
                                    }
                                }
                            }

                            //  Archivar las opciones que ya no llegan desde el front:
                            for (OptionItem opt : targetQ.getOptions()) {
                                if (opt.getId() != null &&
                                        !touchedOptionIds.contains(opt.getId())) {
                                    // En vez de borrar, las marcamos como archivadas
                                    opt.setArchived(true);
                                }
                            }

                            updatedQuestions.add(targetQ);
                        }
                    }

                    // Asignar a la entidad original la lista actualizada
                    existingQuiz.setQuestions(updatedQuestions);

                    Quiz saved = quizDao.save(existingQuiz);

                    // Limpiar opciones archivadas antes de devolver al front
                    limpiarOpcionesArchivadas(saved);

                    return ResponseEntity.ok(saved);
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

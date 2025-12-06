package com.cybersecurityApp.cybersecurity_App.service;

import com.cybersecurityApp.cybersecurity_App.model.OptionItem;
import com.cybersecurityApp.cybersecurity_App.model.Question;
import com.cybersecurityApp.cybersecurity_App.model.Quiz;
import com.cybersecurityApp.cybersecurity_App.model.dao.QuizDao;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class AdminQuizService {

    private final QuizDao quizDao;

    public AdminQuizService(QuizDao quizDao) {
        this.quizDao = quizDao;
    }

    // --- LECTURA ---
    public List<Quiz> getAllQuizzes() {
        return quizDao.findAll();
    }

    public Quiz getQuizById(Long id) {
        return quizDao.findById(id).orElse(null);
    }

    // --- CREACIÓN (Limpia y Ordenada) ---
    @Transactional
    public Quiz createQuiz(Quiz quiz) {
        if (quiz.getQuestions() != null) {
            int qIndex = 1;
            for (Question q : quiz.getQuestions()) {
                q.setQuiz(quiz); // Vincular Padre
                q.setOrd(qIndex++);

                if (q.getOptions() != null) {
                    int oIndex = 1;
                    for (OptionItem opt : q.getOptions()) {
                        opt.setQuestion(q); // Vincular Padre
                        opt.setOrd(oIndex++);
                    }
                }
            }
        }
        return quizDao.save(quiz);
    }

    // --- ACTUALIZACIÓN PROFESIONAL (Smart Merge) ---
    @Transactional
    public Quiz updateQuiz(Long id, Quiz incomingData) {
        Quiz existingQuiz = quizDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Quiz no encontrado con ID: " + id));

        // 1. Actualizar datos básicos
        existingQuiz.setTitle(incomingData.getTitle());
        existingQuiz.setDescription(incomingData.getDescription());
        existingQuiz.setPublished(incomingData.isPublished());

        // 2. Actualizar Preguntas (Sin borrar todo a lo loco)
        mergeQuestions(existingQuiz, incomingData.getQuestions());

        return quizDao.save(existingQuiz);
    }

    // --- BORRADO ---
    @Transactional
    public void deleteQuiz(Long id) {
        quizDao.deleteById(id);
    }

    // ==========================================
    // MÉTODOS PRIVADOS DE LÓGICA DE MEZCLA (MERGE)
    // ==========================================

    private void mergeQuestions(Quiz existingQuiz, List<Question> incomingQuestions) {
        if (incomingQuestions == null) {
            existingQuiz.getQuestions().clear();
            return;
        }

        // Mapa de preguntas actuales por ID para búsqueda rápida
        Map<Long, Question> currentMap = existingQuiz.getQuestions().stream()
                .collect(Collectors.toMap(Question::getId, Function.identity()));

        List<Question> updatedList = new ArrayList<>();
        int qOrd = 1;

        for (Question incoming : incomingQuestions) {
            Question questionToSave;

            if (incoming.getId() != null && currentMap.containsKey(incoming.getId())) {
                // A) LA PREGUNTA YA EXISTE: La actualizamos (Mantenemos el ID)
                questionToSave = currentMap.get(incoming.getId());
                questionToSave.setText(incoming.getText());
                questionToSave.setOrd(qOrd++);

                // Recursivamente hacemos lo mismo con las opciones
                mergeOptions(questionToSave, incoming.getOptions());
            } else {
                // B) ES NUEVA: La preparamos
                questionToSave = incoming;
                questionToSave.setQuiz(existingQuiz); // Importante reconectar
                questionToSave.setOrd(qOrd++);
                // Asegurar relaciones de opciones nuevas
                if (questionToSave.getOptions() != null) {
                    int oOrd = 1;
                    for (OptionItem opt : questionToSave.getOptions()) {
                        opt.setQuestion(questionToSave);
                        opt.setOrd(oOrd++);
                    }
                }
            }
            updatedList.add(questionToSave);
        }

        // C) Reemplazamos la lista.
        // Hibernate detectará:
        // - Cuáles se quedaron (Update)
        // - Cuáles son nuevas (Insert)
        // - Cuáles faltan (Delete - Orphan Removal)
        existingQuiz.getQuestions().clear();
        existingQuiz.getQuestions().addAll(updatedList);
    }

    private void mergeOptions(Question existingQuestion, List<OptionItem> incomingOptions) {
        if (incomingOptions == null) {
            existingQuestion.getOptions().clear();
            return;
        }

        Map<Long, OptionItem> currentMap = existingQuestion.getOptions().stream()
                .collect(Collectors.toMap(OptionItem::getId, Function.identity()));

        List<OptionItem> updatedList = new ArrayList<>();
        int oOrd = 1;

        for (OptionItem incoming : incomingOptions) {
            OptionItem optionToSave;

            if (incoming.getId() != null && currentMap.containsKey(incoming.getId())) {
                // Actualizar existente
                optionToSave = currentMap.get(incoming.getId());
                optionToSave.setText(incoming.getText());
                optionToSave.setCorrect(incoming.isCorrect());
                optionToSave.setOrd(oOrd++);
            } else {
                // Crear nueva
                optionToSave = incoming;
                optionToSave.setQuestion(existingQuestion);
                optionToSave.setOrd(oOrd++);
            }
            updatedList.add(optionToSave);
        }

        existingQuestion.getOptions().clear();
        existingQuestion.getOptions().addAll(updatedList);
    }
}
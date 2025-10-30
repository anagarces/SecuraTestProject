package com.cybersecurityApp.cybersecurity_App.service;

import com.cybersecurityApp.cybersecurity_App.model.*;
import com.cybersecurityApp.cybersecurity_App.model.dao.OptionItemDao;
import com.cybersecurityApp.cybersecurity_App.model.dao.SubmissionDao;
import com.cybersecurityApp.cybersecurity_App.model.dto.SubmissionAnswerDTO;
import com.cybersecurityApp.cybersecurity_App.model.dto.SubmissionRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SubmissionService {

    @Autowired
    private SubmissionDao submissionDao;

    @Autowired
    private OptionItemDao optionItemDao;

    /**
     * Procesa un cuestionario enviado por un usuario:
     * - Calcula el puntaje
     * - Guarda la Submission y sus respuestas
     * - Devuelve el objeto Submission persistido
     */
    public Submission processSubmission(SubmissionRequestDTO dto) {

        // Crear la entidad principal Submission
        Submission submission = new Submission();
        submission.setUser(new Usuario(dto.getUserId())); // Crea referencia al usuario
        submission.setQuiz(new Quiz(dto.getQuizId())); // Crea referencia al quiz

        int score = 0;
        List<SubmissionAnswer> answerEntities = new ArrayList<>();

        //Recorrer todas las respuestas que llegan desde el DTO
        for (SubmissionAnswerDTO answer : dto.getAnswers()) {

            // Buscar la opción selec cionada en la BD
            OptionItem selectedOption = optionItemDao.findById(answer.getOptionId())
                    .orElseThrow(() -> new RuntimeException("Opción no encontrada con ID: " + answer.getOptionId()));

            // Verificar si es correcta y sumar puntaje
            if (selectedOption.isCorrect()) {
                score++;
            }

            // Crear el SubmissionAnswer (una respuesta individual)
            SubmissionAnswer submissionAnswer = new SubmissionAnswer();
            submissionAnswer.setSubmission(submission);
            submissionAnswer.setQuestion(new Question(answer.getQuestionId()));
            submissionAnswer.setOption(selectedOption);

            answerEntities.add(submissionAnswer);
        }

        // 3️⃣ Asignar resultados al Submission
        submission.setScore(score);
        submission.setAnswers(answerEntities);

        //Guardar en la bbdd
        Submission savedSubmission = submissionDao.save(submission);

        //Devolver el resultado guardado
        return savedSubmission;
    }
}

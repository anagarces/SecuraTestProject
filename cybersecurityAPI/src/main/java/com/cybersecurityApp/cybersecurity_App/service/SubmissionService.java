package com.cybersecurityApp.cybersecurity_App.service;

import com.cybersecurityApp.cybersecurity_App.model.*;
import com.cybersecurityApp.cybersecurity_App.model.dao.*;
import com.cybersecurityApp.cybersecurity_App.model.dto.SubmissionAnswerDTO;
import com.cybersecurityApp.cybersecurity_App.model.dto.SubmissionRequestDTO;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


/**
 * Servicio encargado de procesar un envío (submission) de un cuestionario.
 * Aquí se registra qué usuario respondió, sus respuestas, el puntaje obtenido, etc.
 */
@Service
public class SubmissionService {


    private final SubmissionDao submissionDao;
    private final OptionItemDao optionItemDao; // DAO para las opciones de pregunta
    private final QuizDao quizDao;
    private final UserDao userDao;
    private final QuestionDao questionDao;

    public SubmissionService(SubmissionDao submissionDao, OptionItemDao optionItemDao,
                             QuizDao quizDao, UserDao userDao, QuestionDao questionDao) {
        this.submissionDao = submissionDao;
        this.optionItemDao = optionItemDao;
        this.quizDao = quizDao;
        this.userDao = userDao;
        this.questionDao = questionDao;
    }


    /**
     * Procesa el envío del cuestionario.
     * Crea una Submission asociada al usuario autenticado y al quiz.
     * Guarda también las respuestas individuales y calcula el puntaje.
     */
    @Transactional
    public Submission processSubmission(SubmissionRequestDTO dto) {

        //crea entidad principal
        Submission submission = new Submission();

        // Buscar el quiz por su ID (debe existir)
        Quiz quiz = quizDao.findById(dto.getQuizId())
                .orElseThrow(() -> new IllegalStateException("Quiz no encontrado"));
        submission.setQuiz(quiz);

        //  Determinar el usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Validar que realmente haya un usuario logueado
        if (authentication == null || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            throw new IllegalStateException("No se pudo determinar el usuario autenticado");
        }

        // Obtener el email (viene dentro del token JWT)
        String email = authentication.getName();
        // Buscar el usuario en la base de datos
        Usuario user = userDao.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Usuario no encontrado: " + email));


        // Asociar el usuario a esta submission
        submission.setUser(user);

        //  Inicializar campos
        int score = 0; // contador de respuestas correctas
        submission.setSubmittedAt(LocalDateTime.now()); // momento en que se envía
        submission.setFinishedAt(LocalDateTime.now());

        //  Recorrer las respuestas del DTO
        for (SubmissionAnswerDTO answer : dto.getAnswers()) {

            // Buscar la opción seleccionada
            OptionItem selected = optionItemDao.findById(answer.getOptionId())
                    .orElseThrow(() -> new IllegalStateException("Opción no encontrada"));


            // Crear la entidad SubmissionAnswer (respuesta individual)
            SubmissionAnswer subAnswer = new SubmissionAnswer();
            subAnswer.setSubmission(submission);          // vínculo con la submission principal
            subAnswer.setQuestion(selected.getQuestion()); // la pregunta a la que responde
            subAnswer.setOption(selected);                 // la opción elegida

            // Añadir esta respuesta a la lista dentro de Submission
            submission.getAnswers().add(subAnswer);

            // Si la opción era correcta, sumamos 1 punto
            if (selected.isCorrect()) {
                score++;
            }

        }

        //  Guardar resultados generales
        submission.setScore(score);
        submission.setTotalQuestions(dto.getAnswers().size());

        //
        // Gracias al cascade = CascadeType.ALL en Submission, se guardan también las respuestas hijas
        submissionDao.save(submission);

        // Devolver la submission guardada (por si el controlador necesita información)
        return submission;
    }
}

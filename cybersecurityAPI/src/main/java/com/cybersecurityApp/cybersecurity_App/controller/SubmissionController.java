package com.cybersecurityApp.cybersecurity_App.controller;


import com.cybersecurityApp.cybersecurity_App.model.Submission;
import com.cybersecurityApp.cybersecurity_App.model.Usuario;
import com.cybersecurityApp.cybersecurity_App.model.dao.SubmissionDao;
import com.cybersecurityApp.cybersecurity_App.model.dao.UserDao;
import com.cybersecurityApp.cybersecurity_App.model.dto.SubmissionRequestDTO;
import com.cybersecurityApp.cybersecurity_App.model.dto.SubmissionSummaryDTO;
import com.cybersecurityApp.cybersecurity_App.service.SubmissionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/submissions")
@CrossOrigin(origins = "http://localhost:4200")
public class SubmissionController {

    private final SubmissionService submissionService;
    private final SubmissionDao submissionDao;
    private final UserDao userDao;

    public SubmissionController(SubmissionService submissionService, SubmissionDao submissionDao, UserDao userDao) {
        this.submissionService = submissionService;
        this.submissionDao = submissionDao;
        this.userDao = userDao;
    }

    @PostMapping
    public ResponseEntity<?> submitQuiz(@RequestBody SubmissionRequestDTO dto) {
        Submission submission = submissionService.processSubmission(dto);

        // Preparamos una respuesta limpia
        return ResponseEntity.ok(new SubmissionResult(
                submission.getQuiz().getId(),
                submission.getScore(),
                submission.getTotalQuestions()
        ));
    }

    // DTO simple de respuesta
    private record SubmissionResult(Long quizId, int score, int totalQuestions) {}

    @GetMapping("/my")
    public ResponseEntity<List<SubmissionSummaryDTO>> getMySubmissions() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Usuario user = userDao.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Usuario no encontrado"));

        List<Submission> submissions = submissionDao.findByUserIdOrderBySubmittedAtDesc(user.getId());

        List<SubmissionSummaryDTO> result = submissions.stream()
                .map(s -> new SubmissionSummaryDTO(
                        s.getId(),
                        (s.getQuiz() != null ? s.getQuiz().getId() : null),
                        (s.getQuiz() != null ? s.getQuiz().getTitle() : null),
                        s.getScore(),
                        s.getTotalQuestions(),
                        s.getSubmittedAt(),
                        s.getFinishedAt()
                ))
                .toList();

        return ResponseEntity.ok(result);
    }


}

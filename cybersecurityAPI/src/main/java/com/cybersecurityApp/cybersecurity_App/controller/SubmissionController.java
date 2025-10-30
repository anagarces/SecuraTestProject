package com.cybersecurityApp.cybersecurity_App.controller;


import com.cybersecurityApp.cybersecurity_App.model.Submission;
import com.cybersecurityApp.cybersecurity_App.model.dto.SubmissionRequestDTO;
import com.cybersecurityApp.cybersecurity_App.service.SubmissionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/submissions")
@CrossOrigin(origins = "http://localhost:4200")
public class SubmissionController {

    private final SubmissionService submissionService;

    public SubmissionController(SubmissionService submissionService) {
        this.submissionService = submissionService;
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

}

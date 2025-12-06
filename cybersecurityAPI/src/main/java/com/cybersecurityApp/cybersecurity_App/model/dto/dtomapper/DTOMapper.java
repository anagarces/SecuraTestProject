package com.cybersecurityApp.cybersecurity_App.model.dto.dtomapper;

import com.cybersecurityApp.cybersecurity_App.model.Quiz;
import com.cybersecurityApp.cybersecurity_App.model.dto.OptionItemStudentDTO;
import com.cybersecurityApp.cybersecurity_App.model.dto.QuestionStudentDTO;
import com.cybersecurityApp.cybersecurity_App.model.dto.QuizDetailDTO;
import com.cybersecurityApp.cybersecurity_App.model.dto.QuizSummaryDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DTOMapper {

    public QuizDetailDTO toQuizDetailDTO(Quiz quiz) {
        QuizDetailDTO dto = new QuizDetailDTO();
        dto.setId(quiz.getId());
        dto.setTitle(quiz.getTitle());
        dto.setDescription(quiz.getDescription());

        if (quiz.getQuestions() != null) {
            List<QuestionStudentDTO> questionDTOs = quiz.getQuestions().stream().map(q -> {
                QuestionStudentDTO qdto = new QuestionStudentDTO();
                qdto.setId(q.getId());
                qdto.setText(q.getText());

                if (q.getOptions() != null) {
                    List<OptionItemStudentDTO> optionDTOs = q.getOptions().stream().map(o -> {
                        OptionItemStudentDTO odto = new OptionItemStudentDTO();
                        odto.setId(o.getId());
                        odto.setText(o.getText());
                        return odto;
                    }).toList();
                    qdto.setOptions(optionDTOs);
                }
                return qdto;
            }).toList();

            dto.setQuestions(questionDTOs);
        }

        return dto;
    }

    public QuizSummaryDTO toQuizSummaryDTO(Quiz quiz) {
        QuizSummaryDTO dto = new QuizSummaryDTO();
        dto.setId(quiz.getId());
        dto.setTitle(quiz.getTitle());
        dto.setDescription(quiz.getDescription());
        dto.setPublished(quiz.isPublished());
        return dto;
    }
}
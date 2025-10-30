package com.cybersecurityApp.cybersecurity_App.model.dto.dtomapper;

import com.cybersecurityApp.cybersecurity_App.model.Quiz;
import com.cybersecurityApp.cybersecurity_App.model.dto.OptionItemDTO;
import com.cybersecurityApp.cybersecurity_App.model.dto.QuestionDTO;
import com.cybersecurityApp.cybersecurity_App.model.dto.QuizDetailDTO;
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
            List<QuestionDTO> questionDTOs = quiz.getQuestions().stream().map(q -> {
                QuestionDTO qdto = new QuestionDTO();
                qdto.setId(q.getId());
                qdto.setText(q.getText());
                if (q.getOptions() != null) {
                    List<OptionItemDTO> optionDTOs = q.getOptions().stream().map(o -> {
                        OptionItemDTO odto = new OptionItemDTO();
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
}

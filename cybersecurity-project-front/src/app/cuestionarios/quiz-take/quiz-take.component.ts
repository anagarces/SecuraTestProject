import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Quiz } from 'src/app/model/quiz';
import { SubmissionRequest, SubmissionResponse } from 'src/app/model/submission';
import { SubmissionAnswer } from 'src/app/model/submission-answer';
import { QuizService } from 'src/app/services/quiz.service';
import { SubmissionService } from 'src/app/services/submission.service';

@Component({
  selector: 'app-quiz-take',
  templateUrl: './quiz-take.component.html',
  styleUrls: ['./quiz-take.component.css']
})
export class QuizTakeComponent implements OnInit{

   quiz!: Quiz;   // cuestionario cargado desde backend
  selectedAnswers: { [key: number]: number } = {}; // almacena qué opción eligió el usuario por pregunta
  result: SubmissionResponse | null = null; // resultado final devuelto por backend
  userId = 1; // // se reemplazará con el id real del usuario logueado

  constructor(
    private route: ActivatedRoute, // para obtener el ID desde la URL
    private quizService: QuizService, // para obtener el quiz
    private submissionService: SubmissionService // para enviar respuestas
  ) {}

  ngOnInit(): void {
      // Obtener ID de cuestionario desde la ruta actual (ej. /quiz/3)
    const quizId = Number(this.route.snapshot.paramMap.get('id'));
      // Llamar al servicio para traer el cuestionario
    this.quizService.getById(quizId).subscribe({
      next: (data) => this.quiz = data,
      error: (err) => console.error('Error al cargar el cuestionario:', err)
    });
  }

  
  // Cuando el usuario selecciona una opción
  selectOption(questionId: number, optionId: number): void {
    this.selectedAnswers[questionId] = optionId;
  }

   // Cuando el usuario envía el formulario
  submitQuiz(): void {
    // Convertimos las respuestas elegidas en un array de objetos {questionId, optionId}
    const answers: SubmissionAnswer[] = Object.keys(this.selectedAnswers).map(qId => ({
      questionId: Number(qId),
      optionId: this.selectedAnswers[Number(qId)]
    }));

    // Creamos el objeto que se enviará al backend
    const submission: SubmissionRequest = {
      userId: this.userId,
      quizId: this.quiz.id,
      answers
    };

    // Llamamos al servicio para enviar las respuestas al backend
    this.submissionService.submit(submission).subscribe({
      next: (response) => this.result = response,
      error: (err) => console.error('Error al enviar respuestas:', err)
    });
  }

}

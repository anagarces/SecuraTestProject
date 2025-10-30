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
quiz: any; // Cuestionario cargado desde el backend
  answers: { [questionId: number]: number } = {}; // Respuestas seleccionadas por el usuario
  result: any = null; // Resultado del envío (score)
  userId: number = 1; // ⚠️ Cambia esto cuando obtengas el ID real del usuario logueado

  constructor(
    private route: ActivatedRoute,
    private quizService: QuizService,
    private submissionService: SubmissionService
  ) {}

  ngOnInit(): void {
    const quizId = this.route.snapshot.paramMap.get('id');
    if (quizId) {
      this.quizService.getQuizById(+quizId).subscribe({
        next: (data) => {
          this.quiz = data;
          console.log('Quiz cargado:', this.quiz);
        },
        error: (err) => console.error('Error cargando quiz', err)
      });
    }
  }

  submitQuiz(): void {
    if (!this.quiz || Object.keys(this.answers).length === 0) {
      alert('Por favor responde al menos una pregunta.');
      return;
    }

    const submission = {
      userId: this.userId,
      quizId: this.quiz.id,
      answers: Object.keys(this.answers).map(qId => ({
        questionId: Number(qId),
        optionId: this.answers[Number(qId)]
      }))
    };

    console.log('Enviando submission:', submission);

    this.submissionService.submit(submission).subscribe({
      next: (result) => {
        console.log('Resultado recibido:', result);
        this.result = result; // Guardamos el resultado para mostrarlo
      },
      error: (err) => {
        console.error('Error enviando quiz', err);
        alert('Ocurrió un error al enviar tus respuestas.');
      }
    });
  }
}

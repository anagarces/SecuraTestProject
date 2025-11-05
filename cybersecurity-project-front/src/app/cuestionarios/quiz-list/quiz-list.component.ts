import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { QuizSummary } from 'src/app/model/quiz-summary';
import { QuizService } from 'src/app/services/quiz.service';

@Component({
  selector: 'app-quiz-list',
  templateUrl: './quiz-list.component.html',
  styleUrls: ['./quiz-list.component.css']
})
export class QuizListComponent implements OnInit{
  
    quizzes: QuizSummary[] = []; // lista de cuestionarios que vendrá del backend

  constructor(private quizService: QuizService, private router: Router) {}

  ngOnInit(): void {

    // Cuando se carga el componente, obtenemos todos los cuestionarios
    this.quizService.getAllQuizzes().subscribe({
      next: (data) => this.quizzes = data, // guardamos la respuesta
      error: (err) => console.error('Error cargando quizzes:', err) // manejamos errores
    });
  }

  // Navega al componente "quiz-take" para resolver el cuestionario seleccionado
  openQuiz(id: number): void {
    this.router.navigate(['/quiz', id]);
  }

}

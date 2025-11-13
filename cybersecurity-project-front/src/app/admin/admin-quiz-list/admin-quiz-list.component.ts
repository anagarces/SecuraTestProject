import { Component, OnInit } from '@angular/core';
import { AdminQuizService } from '../services/admin-quiz.service';
import { Quiz } from '../../model/quiz';
import { Router } from '@angular/router';

@Component({
  selector: 'app-admin-quiz-list',
  templateUrl: './admin-quiz-list.component.html'
})
export class AdminQuizListComponent implements OnInit {

  quizzes: Quiz[] = [];
  loading = true;

  constructor(
    private adminQuizService: AdminQuizService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadQuizzes();
  }

  loadQuizzes() {
    this.adminQuizService.getAll().subscribe({
      next: data => {
        this.quizzes = data;
        this.loading = false;
      },
      error: err => {
        console.error('Error cargando cuestionarios', err);
      }
    });
  }

  createQuiz() {
    this.router.navigate(['/admin/quizzes/create']);
  }

  editQuiz(id: number) {
    this.router.navigate(['/admin/quizzes/edit', id]);
  }

  deleteQuiz(id: number) {
    if (!confirm('¿Seguro que deseas eliminar este cuestionario?')) return;

    this.adminQuizService.delete(id).subscribe(() => {
      this.loadQuizzes();
    });
  }
}

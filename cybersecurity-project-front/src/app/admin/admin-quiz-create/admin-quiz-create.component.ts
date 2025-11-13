import { Component } from '@angular/core';
import { AdminQuizService } from '../services/admin-quiz.service';
import { Router } from '@angular/router';
import { Quiz } from '../../model/quiz';

@Component({
  selector: 'app-admin-quiz-create',
  templateUrl: './admin-quiz-create.component.html'
})
export class AdminQuizCreateComponent {

  quiz: Quiz = {
    title: '',
    description: '',
    questions: []
  };

  constructor(
    private adminQuizService: AdminQuizService,
    private router: Router
  ) {}

  saveQuiz() {
    this.adminQuizService.create(this.quiz).subscribe(() => {
      this.router.navigate(['/admin/quizzes']);
    });
  }
}

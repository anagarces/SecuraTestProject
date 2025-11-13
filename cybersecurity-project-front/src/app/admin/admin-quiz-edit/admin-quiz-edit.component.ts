import { Component, OnInit } from '@angular/core';
import { AdminQuizService } from '../services/admin-quiz.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Quiz } from '../../model/quiz';

@Component({
  selector: 'app-admin-quiz-edit',
  templateUrl: './admin-quiz-edit.component.html'
})
export class AdminQuizEditComponent implements OnInit {

  quiz: Quiz | undefined;
  id!: number;

  constructor(
    private route: ActivatedRoute,
    private adminQuizService: AdminQuizService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.id = Number(this.route.snapshot.paramMap.get('id'));
    this.loadQuiz();
  }

  loadQuiz() {
    this.adminQuizService.getById(this.id).subscribe(q => this.quiz = q);
  }

  saveChanges() {
    if (!this.quiz) return;

    this.adminQuizService.update(this.id, this.quiz).subscribe(() => {
      this.router.navigate(['/admin/quizzes']);
    });
  }
}

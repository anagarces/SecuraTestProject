import { Component } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AdminQuizService } from '../services/admin-quiz.service';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-admin-quiz-create',
  templateUrl: './admin-quiz-create.component.html'
})
export class AdminQuizCreateComponent {

  quizForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private adminQuizService: AdminQuizService,
    private router: Router,
    private snackBar: MatSnackBar
  ) {
    this.quizForm = this.fb.group({
      title: ['', Validators.required],
      description: ['', Validators.required],
      questions: this.fb.array([])
    });
  }

  get questions(): FormArray {
    return this.quizForm.get('questions') as FormArray;
  }

  getOptions(qi: number): FormArray {
    return this.questions.at(qi).get('options') as FormArray;
  }

  addQuestion() {
    const questionGroup = this.fb.group({
      text: ['', Validators.required],
      options: this.fb.array([])
    });

    this.questions.push(questionGroup);
  }

  addOption(qi: number) {
    const optionGroup = this.fb.group({
      text: ['', Validators.required],
      correct: [false]   // 👈 ahora se llama 'correct'
    });

    this.getOptions(qi).push(optionGroup);
  }

  setCorrectOption(qi: number, oi: number) {
    const options = this.getOptions(qi);

    options.controls.forEach((group, i) => {
      group.get('correct')?.setValue(i === oi);
    });
  }

  saveQuiz() {
    if (this.quizForm.invalid) {
      this.snackBar.open('Completa todos los campos antes de guardar.', 'Cerrar', { duration: 2500 });
      return;
    }

    const quizData = this.quizForm.value;

    this.adminQuizService.create(quizData).subscribe({
      next: () => {
        this.snackBar.open('Cuestionario creado correctamente', 'OK', { duration: 2000 });
        this.router.navigate(['/admin/quizzes']);
      },
      error: err => {
        console.error(err);
        this.snackBar.open('Error al crear el cuestionario', 'Cerrar', { duration: 2500 });
      }
    });
  }
}

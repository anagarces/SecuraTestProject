import { Component, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AdminQuizService } from '../services/admin-quiz.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-admin-quiz-edit',
  templateUrl: './admin-quiz-edit.component.html'
})
export class AdminQuizEditComponent implements OnInit {

  quizForm!: FormGroup;
  quizId!: number;
  loading = true;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private adminQuizService: AdminQuizService,
    private snackBar: MatSnackBar,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.quizId = Number(this.route.snapshot.paramMap.get('id'));
    this.loadQuiz();
  }

  loadQuiz() {
    this.adminQuizService.getById(this.quizId).subscribe({
      next: quiz => {
        this.buildForm(quiz);
        this.loading = false;
      },
      error: err => {
        console.error(err);
        this.snackBar.open('Error cargando el cuestionario', 'Cerrar', { duration: 2500 });
      }
    });
  }

  buildForm(quiz: any) {
    this.quizForm = this.fb.group({
      title: [quiz.title, Validators.required],
      description: [quiz.description, Validators.required],
      questions: this.fb.array([])
    });

    quiz.questions.forEach((q: any) => {
      const questionGroup = this.fb.group({
        id: [q.id],
        text: [q.text, Validators.required],
        options: this.fb.array([])
      });

      q.options.forEach((opt: any) => {
        const optionGroup = this.fb.group({
          id: [opt.id],
          text: [opt.text, Validators.required],
          correct: [opt.correct]   // 👈 coincide con backend
        });

        (questionGroup.get('options') as FormArray).push(optionGroup);
      });

      this.questions.push(questionGroup);
    });
  }

  // Getters
  get questions(): FormArray {
    return this.quizForm.get('questions') as FormArray;
  }

  getOptions(qi: number): FormArray {
    return this.questions.at(qi).get('options') as FormArray;
  }

  // Agregar / eliminar
  addQuestion() {
    this.questions.push(
      this.fb.group({
        id: [null],
        text: ['', Validators.required],
        options: this.fb.array([])
      })
    );
  }

  deleteQuestion(index: number) {
    // Opción A: solo lo quitamos del formulario.
    // El backend NO borrará la pregunta en BD.
    this.questions.removeAt(index);
  }

  addOption(qi: number) {
    this.getOptions(qi).push(
      this.fb.group({
        id: [null],
        text: ['', Validators.required],
        correct: [false]
      })
    );
  }

  deleteOption(qi: number, oi: number) {
    // Igual que deleteQuestion: solo se quita del formulario.
    this.getOptions(qi).removeAt(oi);
  }

  setCorrectOption(qi: number, oi: number) {
    const options = this.getOptions(qi);

    options.controls.forEach((group, i) => {
      group.get('correct')?.setValue(i === oi);
    });
  }

  saveQuiz() {
    if (this.quizForm.invalid) {
      this.snackBar.open('Completa todos los campos', 'Cerrar', { duration: 2000 });
      return;
    }

    const quizData = this.quizForm.value;

    this.adminQuizService.update(this.quizId, quizData).subscribe({
      next: () => {
        this.snackBar.open('Cuestionario actualizado', 'OK', { duration: 2000 });
        this.router.navigate(['/admin/quizzes']);
      },
      error: err => {
        console.error(err);
        this.snackBar.open('Error al guardar', 'Cerrar', { duration: 2500 });
      }
    });
  }
}

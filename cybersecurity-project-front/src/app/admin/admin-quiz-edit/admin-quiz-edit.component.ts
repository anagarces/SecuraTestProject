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
        this.cancel(); // Regresar si falla
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
          correct: [opt.correct]
        });

        (questionGroup.get('options') as FormArray).push(optionGroup);
      });

      this.questions.push(questionGroup);
    });
  }

  // --- Getters ---
  get questions(): FormArray {
    return this.quizForm.get('questions') as FormArray;
  }

  getOptions(qi: number): FormArray {
    return this.questions.at(qi).get('options') as FormArray;
  }

  // --- Agregar ---
  addQuestion() {
    this.questions.push(
      this.fb.group({
        id: [null], // Nuevo, sin ID
        text: ['', Validators.required],
        options: this.fb.array([])
      })
    );
    // Agregamos opciones por defecto
    const newIndex = this.questions.length - 1;
    this.addOption(newIndex);
    this.addOption(newIndex);
  }

  addOption(qi: number) {
    this.getOptions(qi).push(
      this.fb.group({
        id: [null], // Nuevo, sin ID
        text: ['', Validators.required],
        correct: [false]
      })
    );
  }

  // --- Eliminar (Consistente con Crear) ---
  removeQuestion(index: number) {
    if (this.questions.length <= 1) {
      this.snackBar.open('No puedes dejar el cuestionario vacío.', 'Cerrar', { duration: 2000 });
      return;
    }
    if (confirm('¿Eliminar esta pregunta? Si guardas cambios, se borrará permanentemente.')) {
      this.questions.removeAt(index);
    }
  }

  removeOption(qi: number, oi: number) {
    const options = this.getOptions(qi);
    if (options.length <= 2) {
      this.snackBar.open('Mínimo 2 opciones requeridas.', 'Cerrar', { duration: 2000 });
      return;
    }
    options.removeAt(oi);
  }

  setCorrectOption(qi: number, oi: number) {
    const options = this.getOptions(qi);
    options.controls.forEach((group, i) => {
      group.get('correct')?.setValue(i === oi);
    });
  }

  cancel() {
    this.router.navigate(['/admin/quizzes']);
  }

  // --- Guardar ---
  saveQuiz() {
    if (this.quizForm.invalid) {
      this.quizForm.markAllAsTouched();
      this.snackBar.open('Completa todos los campos obligatorios.', 'Cerrar', { duration: 2000 });
      return;
    }

    const quizData = this.quizForm.value;

    this.adminQuizService.update(this.quizId, quizData).subscribe({
      next: () => {
        this.snackBar.open('Cuestionario actualizado correctamente', 'OK', { duration: 2000 });
        this.router.navigate(['/admin/quizzes']);
      },
      error: err => {
        console.error(err);
        this.snackBar.open('Error al guardar los cambios', 'Cerrar', { duration: 2500 });
      }
    });
  }
}

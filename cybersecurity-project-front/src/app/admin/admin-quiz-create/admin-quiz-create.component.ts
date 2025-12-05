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

    // Iniciamos con una pregunta por defecto para no mostrar el formulario vacío
    this.addQuestion();
  }

  // --- Getters ---
  get questions(): FormArray {
    return this.quizForm.get('questions') as FormArray;
  }

  getOptions(qi: number): FormArray {
    return this.questions.at(qi).get('options') as FormArray;
  }

  // --- Lógica de Agregar ---
  addQuestion() {
    const questionGroup = this.fb.group({
      text: ['', Validators.required],
      options: this.fb.array([])
    });

    this.questions.push(questionGroup);

    // Agregamos 2 opciones por defecto a la nueva pregunta
    const newIndex = this.questions.length - 1;
    this.addOption(newIndex);
    this.addOption(newIndex);
  }

  addOption(qi: number) {
    const optionGroup = this.fb.group({
      text: ['', Validators.required],
      correct: [false]
    });

    this.getOptions(qi).push(optionGroup);
  }

  // --- Lógica de Eliminar (NUEVO) ---
  removeQuestion(index: number) {
    if (this.questions.length <= 1) {
      this.snackBar.open('El cuestionario debe tener al menos una pregunta.', 'Cerrar', { duration: 2000 });
      return;
    }
    
    // Confirmación simple
    if (confirm('¿Eliminar esta pregunta?')) {
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

  // --- Utilidades ---
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
      this.snackBar.open('Por favor completa todos los campos requeridos.', 'Cerrar', { duration: 2500 });
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
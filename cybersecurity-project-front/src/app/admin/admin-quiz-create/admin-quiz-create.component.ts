import { Component } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AdminQuizService } from '../services/admin-quiz.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-admin-quiz-create',
  templateUrl: './admin-quiz-create.component.html'
})
export class AdminQuizCreateComponent {

  quizForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private adminQuizService: AdminQuizService,
    private router: Router
  ) {

    this.quizForm = this.fb.group({
      title: ['', Validators.required],
      description: ['', Validators.required],
      questions: this.fb.array([]) // array dinámico
    });
  }

  // 🔹 Getter para preguntas
  get questions(): FormArray {
    return this.quizForm.get('questions') as FormArray;
  }

  // 🔹 Getter para opciones
  getOptions(questionIndex: number): FormArray {
    return this.questions.at(questionIndex).get('options') as FormArray;
  }

  // ➕ Agregar pregunta
  addQuestion() {
    const questionGroup = this.fb.group({
      text: ['', Validators.required],
      options: this.fb.array([])
    });

    this.questions.push(questionGroup);
  }

  // ➕ Agregar opción a una pregunta
  addOption(questionIndex: number) {
    const optionGroup = this.fb.group({
      text: ['', Validators.required],
      isCorrect: [false]
    });

    this.getOptions(questionIndex).push(optionGroup);
  }

  // ✔ Marcar una opción como correcta (solo una por pregunta)
  setCorrectOption(questionIndex: number, optionIndex: number) {
    const options = this.getOptions(questionIndex);

    options.controls.forEach((group, i) => {
      group.get('isCorrect')?.setValue(i === optionIndex);
    });
  }

  // 💾 Guardar cuestionario
  saveQuiz() {
    if (this.quizForm.invalid) {
      alert('Completa todos los campos antes de guardar.');
      return;
    }

    const quizData = this.quizForm.value;

    this.adminQuizService.create(quizData).subscribe(() => {
      alert("Cuestionario creado correctamente");
      this.router.navigate(['/admin/quizzes']);
    });
  }
}


import { Component, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AdminQuizService } from '../services/admin-quiz.service';
import { Quiz } from '../../model/quiz';

@Component({
  selector: 'app-admin-quiz-edit',
  templateUrl: './admin-quiz-edit.component.html'
})
export class AdminQuizEditComponent implements OnInit {

  quizId!: number;
  quizForm!: FormGroup;
  loading = true;

  constructor(
    private route: ActivatedRoute,
    private fb: FormBuilder,
    private adminQuizService: AdminQuizService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.quizId = Number(this.route.snapshot.paramMap.get('id'));
    this.loadQuiz();
  }

  /* -------------------------------------------------------
      CARGAR CUESTIONARIO EXISTENTE
  ---------------------------------------------------------*/
  loadQuiz(): void {
    this.adminQuizService.getById(this.quizId).subscribe({
      next: (quiz: Quiz) => {
        this.buildForm(quiz);
        this.loading = false;
      },
      error: err => console.error('Error cargando quiz', err)
    });
  }

  /* -------------------------------------------------------
      CONSTRUIR FORMULARIO DINÁMICO
  ---------------------------------------------------------*/
  buildForm(quiz: Quiz): void {
    this.quizForm = this.fb.group({
      title: [quiz.title, Validators.required],
      description: [quiz.description, Validators.required],
      questions: this.fb.array([])
    });

    // Agregar preguntas existentes
    quiz.questions.forEach(q => {
      const questionGroup = this.fb.group({
        id: [q.id],
        text: [q.text, Validators.required],
        options: this.fb.array([])
      });

      this.questions.push(questionGroup);

      // Agregar opciones
      q.options.forEach(opt => {
        const optionGroup = this.fb.group({
          id: [opt.id],
          text: [opt.text, Validators.required],
          isCorrect: [opt.correct]
        });

        this.getOptions(this.questions.length - 1).push(optionGroup);
      });
    });
  }

  /* -------------------------------------------------------
      GETTERS
  ---------------------------------------------------------*/
  get questions(): FormArray {
    return this.quizForm.get('questions') as FormArray;
  }

  getOptions(qIndex: number): FormArray {
    return this.questions.at(qIndex).get('options') as FormArray;
  }

  /* -------------------------------------------------------
      ACCIONES DINÁMICAS
  ---------------------------------------------------------*/
  addQuestion() {
    this.questions.push(
      this.fb.group({
        text: ['', Validators.required],
        options: this.fb.array([])
      })
    );
  }

  deleteQuestion(index: number) {
    this.questions.removeAt(index);
  }

  addOption(qIndex: number) {
    this.getOptions(qIndex).push(
      this.fb.group({
        text: ['', Validators.required],
        isCorrect: [false]
      })
    );
  }

  deleteOption(qIndex: number, optIndex: number) {
    this.getOptions(qIndex).removeAt(optIndex);
  }

  setCorrectOption(qIndex: number, optIndex: number) {
    const options = this.getOptions(qIndex);
    options.controls.forEach((o, i) => {
      o.get('isCorrect')?.setValue(i === optIndex);
    });
  }

  /* -------------------------------------------------------
      GUARDAR CAMBIOS
  ---------------------------------------------------------*/
  saveQuiz(): void {
    if (this.quizForm.invalid) {
      alert('Completa todo el formulario.');
      return;
    }

    this.adminQuizService.update(this.quizId, this.quizForm.value)
      .subscribe(() => {
        alert('Cuestionario actualizado correctamente');
        this.router.navigate(['/admin/quizzes']);
      });
  }
}


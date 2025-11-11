import { Component, OnInit } from '@angular/core';
import { SubmissionService } from 'src/app/services/submission.service';
import { SubmissionSummary } from 'src/app/model/submission-summary';

@Component({
  selector: 'app-mis-resultados',
  templateUrl: './mis-resultados.component.html'
})
export class MisResultadosComponent implements OnInit {
  resultados: SubmissionSummary[] = [];
  cargando = false;
  error = '';

  constructor(private submissionService: SubmissionService) {}

  ngOnInit(): void {
    this.cargarResultados();
  }

  cargarResultados(): void {
    this.cargando = true;
    this.error = '';
    this.submissionService.getMySubmissions().subscribe({
      next: (data) => {
        this.resultados = data;
        this.cargando = false;
      },
      error: (err) => {
        console.error('Error cargando resultados', err);
        this.error = 'No se pudieron cargar tus resultados.';
        this.cargando = false;
      }
    });
  }

  porcentaje(r: SubmissionSummary): number {
    if (!r || !r.totalQuestions) return 0;
    return Math.round((r.score / r.totalQuestions) * 100);
  }
}

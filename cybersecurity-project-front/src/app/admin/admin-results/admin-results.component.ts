import { Component, OnInit } from '@angular/core';
import { AdminSubmissionService } from '../services/admin-submission.service';

// Importamos la interfaz correcta desde la carpeta model
import { SubmissionSummary } from '../../model/submission-summary';

@Component({
  selector: 'app-admin-results',
  templateUrl: './admin-results.component.html',
  styleUrls: ['./admin-results.component.css']
})
export class AdminResultsComponent implements OnInit {

  // Usamos la interfaz correcta
  results: SubmissionSummary[] = [];
  loading = true;

  constructor(private submissionService: AdminSubmissionService) {}

  ngOnInit(): void {
    this.loadResults();
  }

  loadResults(): void {
    this.loading = true;

    this.submissionService.getAllResults().subscribe({
      next: (data: any) => {
        this.results = data;
        this.loading = false;
      },
      error: (err) => {
        console.error('Error cargando resultados', err);
        this.loading = false;
      }
    });
  }
}
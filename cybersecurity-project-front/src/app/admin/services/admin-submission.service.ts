import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface AdminSubmissionSummary {
  submissionId: number;
  userEmail: string;
  userName: string;
  quizId: number;
  quizTitle: string;
  score: number;
  totalQuestions: number;
  submittedAt: string; 
}

@Injectable({
  providedIn: 'root'
})
export class AdminSubmissionService {

  private baseUrl = 'http://localhost:8080/api/submissions/admin';

  constructor(private http: HttpClient) {}

  // GET /api/submissions/admin/all
  getAllResults(): Observable<AdminSubmissionSummary[]> {
    return this.http.get<AdminSubmissionSummary[]>(`${this.baseUrl}/all`);
  }
}



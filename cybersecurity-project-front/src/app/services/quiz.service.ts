import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Quiz } from '../model/quiz';
import { QuizSummary } from '../model/quiz-summary';
import { SubmissionRequest, SubmissionResponse } from '../model/submission'; 
import { SubmissionSummary } from '../model/submission-summary';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class QuizService {

  private quizzesUrl = 'http://localhost:8080/api/quizzes';
  private submissionsUrl = 'http://localhost:8080/api/submissions'; 

  constructor(private http: HttpClient, private authService: AuthService) {}

  private getAuthHeaders(): HttpHeaders {
    const token = this.authService.getToken();
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
  }

  // --- LECTURA (GET) ---

  getAllQuizzes(): Observable<QuizSummary[]> {
    return this.http.get<QuizSummary[]>(this.quizzesUrl, { headers: this.getAuthHeaders() });
  }

  getQuizById(id: number): Observable<Quiz> {
    return this.http.get<Quiz>(`${this.quizzesUrl}/${id}`, { headers: this.getAuthHeaders() });
  }

  // --- ESCRITURA (POST) - FINAL ---

  /**
   * Envía las respuestas al nuevo endpoint /api/submissions.
   * Utiliza SubmissionRequest como input y SubmissionResponse como output.
   */
  submitQuiz(submission: SubmissionRequest): Observable<SubmissionResponse> {
    return this.http.post<SubmissionResponse>(
      this.submissionsUrl, 
      submission, 
      { headers: this.getAuthHeaders() }
    );
  }
}
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Quiz } from '../model/quiz';
import { QuizSummary } from '../model/quiz-summary';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class QuizService {

  private apiUrl = 'http://localhost:8080/api/quizzes';

  constructor(private http: HttpClient, private authService: AuthService) {}

  private getAuthHeaders(): HttpHeaders {
    const token = this.authService.getToken();
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
  }

  getAllQuizzes(): Observable<QuizSummary[]> {
     return this.http.get<QuizSummary[]>(this.apiUrl, { headers: this.getAuthHeaders() });
  }

  getQuizById(id: number): Observable<Quiz> {
   return this.http.get<Quiz>(`${this.apiUrl}/${id}`, { headers: this.getAuthHeaders() });
  }

    submitQuiz(quizId: number, respuestas: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/${quizId}/submit`, respuestas, { headers: this.getAuthHeaders() });
  }
}

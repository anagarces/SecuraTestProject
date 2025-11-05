import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Quiz } from '../model/quiz';
import { QuizSummary } from '../model/quiz-summary';

@Injectable({
  providedIn: 'root'
})
export class QuizService {

  private apiUrl = 'http://localhost:8080/api/quizzes';

  constructor(private http: HttpClient) {}

  getAllQuizzes(): Observable<QuizSummary[]> {
    return this.http.get<QuizSummary[]>(this.apiUrl);
  }

  getQuizById(id: number): Observable<Quiz> {
    return this.http.get<Quiz>(`${this.apiUrl}/${id}`);
  }
}

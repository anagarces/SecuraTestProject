import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Quiz } from '../model/quiz';

@Injectable({
  providedIn: 'root'
})
export class QuizService {

  private baseUrl = 'http://localhost:8080/api/quizzes';

  private apiUrl = 'http://localhost:8080/api/quizzes';

  constructor(private http: HttpClient) {}

  getAllQuizzes(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }

  getQuizById(id: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/${id}`);
  }
}

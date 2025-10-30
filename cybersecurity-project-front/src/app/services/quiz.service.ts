import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Quiz } from '../model/quiz';

@Injectable({
  providedIn: 'root'
})
export class QuizService {

  private baseUrl = 'http://localhost:8080/api/quizzes';

  constructor(private http: HttpClient) { }

   // Obtiene todos los cuestionarios disponibles desde el backend
  getAll(): Observable<Quiz[]>{

    return this.http.get<Quiz[]>(this.baseUrl);
  }

  // Obtiene un cuestionario específico (por ID)
    getById(id: number): Observable<Quiz> {
    return this.http.get<Quiz>(`${this.baseUrl}/${id}`);
  }
}

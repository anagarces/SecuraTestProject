import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Quiz } from '../../model/quiz';

@Injectable({
  providedIn: 'root'
})
export class AdminQuizService {

  private apiUrl = 'http://localhost:8080/api/admin/quizzes';

  constructor(private http: HttpClient) {}

  private getHeaders() {
    const token = localStorage.getItem('auth_token');
    return {
      headers: new HttpHeaders({
        Authorization: `Bearer ${token}`
      })
    };
  }

  // Obtener todos los cuestionarios
  getAll(): Observable<Quiz[]> {
    return this.http.get<Quiz[]>(this.apiUrl, this.getHeaders());
  }

  // Obtener un cuestionario por ID
  getById(id: number): Observable<Quiz> {
    return this.http.get<Quiz>(`${this.apiUrl}/${id}`, this.getHeaders());
  }

  // Crear cuestionario
  create(quiz: Quiz): Observable<Quiz> {
    return this.http.post<Quiz>(this.apiUrl, quiz, this.getHeaders());
  }

  // Actualizar cuestionario
  update(id: number, quiz: Quiz): Observable<Quiz> {
    return this.http.put<Quiz>(`${this.apiUrl}/${id}`, quiz, this.getHeaders());
  }

  // Eliminar cuestionario
  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`, this.getHeaders());
  }
}

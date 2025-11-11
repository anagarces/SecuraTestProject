import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { SubmissionRequest, SubmissionResponse } from '../model/submission';
import { SubmissionSummary } from '../model/submission-summary';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class SubmissionService {

   private baseUrl = 'http://localhost:8080/api/submissions';

  constructor(private http: HttpClient, private authService: AuthService) { }

  // Envía las respuestas del usuario al backend
  submit(submission: SubmissionRequest): Observable<SubmissionResponse> {
    
    const token = this.authService.getToken();
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
    return this.http.post<SubmissionResponse>(this.baseUrl, submission, { headers });
  }

   getMySubmissions(): Observable<SubmissionSummary[]> {
    const headers = new HttpHeaders({ Authorization: `Bearer ${this.authService.getToken()}` });
    return this.http.get<SubmissionSummary[]>(`${this.baseUrl}/my`, { headers });
  }

}

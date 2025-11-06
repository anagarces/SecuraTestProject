import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { SubmissionRequest, SubmissionResponse } from '../model/submission';
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

}

import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { SubmissionRequest, SubmissionResponse } from '../model/submission';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SubmissionService {

   private baseUrl = 'http://localhost:8080/api/submissions';

  constructor(private http: HttpClient) { }

  // Envía las respuestas del usuario al backend
  submit(submission: SubmissionRequest): Observable<SubmissionResponse> {
    
    return this.http.post<SubmissionResponse>(this.baseUrl, submission);
  }
}

import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../../model/user';

@Injectable({
  providedIn: 'root'
})
export class AdminUserService {

  private apiUrl = 'http://localhost:8080/api/admin/users';

  constructor(private http: HttpClient) {}

  // Obtener todos
  getAll(): Observable<User[]> {
    return this.http.get<User[]>(this.apiUrl);
  }

  // Obtener por ID
  getById(id: number): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/${id}`);
  }

  // Crear usuario
  createUser(data: any): Observable<User> {
    return this.http.post<User>(this.apiUrl, data);
  }

  // Actualizar datos básicos
  updateUser(id: number, data: any): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/${id}`, data);
  }

  // Cambiar rol
  updateRole(id: number, role: string): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/${id}/role`, { role });
  }

  // 🔥 Desactivar usuario (FALTABA)
  disable(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  // Opcional: Reactivar usuario
  reactivate(id: number): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/${id}/reactivate`, {});
  }
}

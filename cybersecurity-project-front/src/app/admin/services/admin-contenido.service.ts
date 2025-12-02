import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IContenido } from 'src/app/model/contenido';

@Injectable({ providedIn: 'root' })
export class AdminContenidoService {

  private api = 'http://localhost:8080/api/admin/contenidos';

  constructor(private http: HttpClient) {}

  getAll(): Observable<IContenido[]> {
    return this.http.get<IContenido[]>(this.api);
  }

  getById(id: number): Observable<IContenido> {
    return this.http.get<IContenido>(`${this.api}/${id}`);
  }

  create(data: IContenido): Observable<IContenido> {
    return this.http.post<IContenido>(this.api, data);
  }

  update(id: number, data: IContenido): Observable<IContenido> {
    return this.http.put<IContenido>(`${this.api}/${id}`, data);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.api}/${id}`);
  }
}


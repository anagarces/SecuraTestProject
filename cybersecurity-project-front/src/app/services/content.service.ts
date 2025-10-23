import { HttpClient} from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { IContenido } from '../model/contenido';

@Injectable({
  providedIn: 'root'
})
export class ContentService {

      private apiUrl = 'http://localhost:8080/contenidos';

  constructor(private http: HttpClient) { }

  //Obtener todos los contenidos de ciberseguridad
  getContenidos(): Observable<IContenido[]>{
    return this.http.get<IContenido[]>(this.apiUrl+'/getAll');
  }

    /**
   * Pide un único elemento de contenido por su ID.
   * Devuelve un Observable de un solo objeto, no un array.
   */
  getContenidoById(id:number): Observable<IContenido>{

    const url = `${this.apiUrl}/${id}`;

    return this.http.get<IContenido>(url);
  }

}

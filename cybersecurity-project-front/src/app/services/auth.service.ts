import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';
import { AuthResponse } from '../model/auth-response';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private apiUrl = 'http://localhost:8080/api/auth';  //url base de la api de autenticacion
  private contentApiUrl = 'http://localhost:8080/contenidos';

  constructor(private http: HttpClient, private router: Router) { }

  /**
   * Llama al endpoint de login del backend.
   * Si es exitoso, guarda el token en el navegador.
   */
  login(credentials: {email: string, password: string}): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, credentials).pipe(
      tap(response => {
        // 'tap' nos permite espiar la respuesta sin modificarla
        // Guardamos el token en el almacenamiento local del navegador
        localStorage.setItem('auth_token', response.token);
      })
    );
  }


    /**
   * Cierra la sesión del usuario.
   */
  logout(): void {
    // Simplemente borramos el token del almacenamiento
    localStorage.removeItem('auth_token');
    // Y redirigimos al usuario a la página de login
    this.router.navigate(['/login']);
  }

    /**
   * Obtiene el token guardado.
   */
  getToken(): string | null {
    return localStorage.getItem('auth_token');
  }

    /**
   * Comprueba si el usuario está logueado (si existe un token).
   */
  isLoggedIn(): boolean {
    // El !! convierte el resultado (string o null) a un booleano (true o false)
    return !!this.getToken();
  }

  register(user: any): Observable<any> {
  return this.http.post<any>(`${this.apiUrl}/register`, user);
}

  getContenidos(): Observable<any> {
    // Reutilizamos tu propio método para obtener el token. ¡Perfecto!
    const token = this.getToken();

    // Creamos la cabecera de autorización con el token.
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });

    // Hacemos la petición a la ruta protegida, pasando las cabeceras.
    return this.http.get(`${this.contentApiUrl}/getAll`, { headers: headers });
  }

}


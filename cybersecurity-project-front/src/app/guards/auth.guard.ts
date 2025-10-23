import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { AuthService } from '../services/auth.service';

@Injectable({
  providedIn: 'root'
})

export class AuthGuard implements CanActivate {
// 1. Inyectamos los servicios que necesitamos en el constructor
  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  // 2. Esta es la lógica principal del guardián
  canActivate(): boolean {
    
    // 3. Preguntamos al servicio si el usuario ha iniciado sesión
    if (this.authService.isLoggedIn()) {
      // Si la respuesta es SÍ, permitimos el paso a la ruta
      return true;
    } else {
      // Si la respuesta es NO, redirigimos al usuario a la página de inicio (login)
      this.router.navigate(['/']);
      // Y denegamos el paso a la ruta protegida
      return false;
    }
  }
  
}

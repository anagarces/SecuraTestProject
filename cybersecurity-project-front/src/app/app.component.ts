import { Component } from '@angular/core';
import { AuthService } from './services/auth.service';
import { Router } from '@angular/router';
import {jwtDecode} from 'jwt-decode';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'cybersecurity-project';
  adminMenuOpen = false;
isLoggingOut = false;
   constructor(public authService: AuthService, private router: Router) {
    (window as any).jwtDecode = jwtDecode;
}
 



logout(): void {
  this.isLoggingOut = true;

  // Animación / sensación de proceso
  setTimeout(() => {

    // Marcamos salida para mostrar mensaje al volver a login
    localStorage.setItem('logout_msg', 'true');

    this.authService.logout(); // limpia token y rol

    this.router.navigate(['/login']);

    // opcional si quieres quitar el loader después
    // setTimeout(() => this.isLoggingOut = false, 300);

  }, 900); // duración del efecto visual
}



toggleAdminMenu() {
  this.adminMenuOpen = !this.adminMenuOpen;
}


}

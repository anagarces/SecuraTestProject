import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'] // Puedes añadir estilos aquí
})

export class LoginComponent {
  credentials = { email: '', password: '' };
  error = '';

  constructor(private authService: AuthService, private router: Router) { }

  onSubmit(): void {
    this.authService.login(this.credentials).subscribe({
      next: (response) => {
        // Si el login es exitoso, redirigimos a la página de contenidos
        localStorage.setItem('auth_token', response.token);
        this.router.navigate(['/contenido']);
      },
      error: (err) => {
        // Si hay un error (ej. 401), mostramos un mensaje
        this.error = 'Credenciales incorrectas. Por favor, inténtalo de nuevo.';
        console.error('Error en el login:', err);
      }
    });
  }
}
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})

export class LoginComponent {

  credentials = { email: '', password: '' };
  error = '';
  loading = false;

  constructor(private authService: AuthService, private router: Router) {}

  onSubmit(): void {

    if (!this.credentials.email || !this.credentials.password) return;

    this.error = '';
    this.loading = true;

    this.authService.login(this.credentials).subscribe({
      next: (response: any) => {

        localStorage.setItem('auth_token', response.token);

        // Recuperamos el nombre que viene del backend (ajusta si tu API lo devuelve distinto)
        localStorage.setItem('welcome_name', response.nombre || 'Usuario');
        localStorage.setItem('welcome_login', 'true');

        // micro pausa agradable para UX
        setTimeout(() => {
          this.loading = false;
          this.router.navigate(['/contenido']); 
        }, 900);
      },

      error: () => {
        this.loading = false;
        this.error = 'Credenciales incorrectas. Por favor, inténtalo de nuevo.';
      }
    });
  }
}

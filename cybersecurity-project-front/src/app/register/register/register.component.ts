// register.component.ts
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  userData = { nombre: '', email: '', password: '' };
  message = '';
  error = '';

  constructor(private authService: AuthService, private router: Router) { }

  onSubmit(): void {
    this.authService.register(this.userData).subscribe({
      next: (response) => {
        this.error = '';
        this.message = '¡Registro exitoso! Serás redirigido a la página de login.';
        // Espera 2 segundos y redirige al login
        setTimeout(() => {
          this.router.navigate(['/']);
        }, 2000);
      },
      error: (err) => {
        this.message = '';
        this.error = err.error.message || 'Error en el registro. Inténtalo de nuevo.';
        console.error('Error en el registro:', err);
      }
    });
  }
}
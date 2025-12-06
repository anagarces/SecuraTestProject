import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { FormBuilder, FormGroup, Validators, AbstractControl, ValidationErrors } from '@angular/forms';

/**
 * Función de validación personalizada para confirmar que dos campos de contraseña coincidan.
 */
export function MustMatchValidator(controlName: string, matchingControlName: string): (formGroup: FormGroup) => ValidationErrors | null {
  return (formGroup: FormGroup) => {
    const control = formGroup.controls[controlName];
    const matchingControl = formGroup.controls[matchingControlName];

    if (matchingControl.errors && !matchingControl.errors['mustMatch']) {
      // Retorna si otro validador ya ha encontrado un error en matchingControl
      return null;
    }

    // Establece el error 'mustMatch' en el campo de confirmación si las contraseñas no coinciden
    if (control.value !== matchingControl.value) {
      matchingControl.setErrors({ mustMatch: true });
      return { mustMatch: true };
    } else {
      matchingControl.setErrors(null);
      return null;
    }
  };
}


@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
 
  registerForm: FormGroup;
  backendErrors: any = {}; //errores que vienen del backend
  message = '';
  loading = false;

  constructor(private authService: AuthService, private router: Router, private fb: FormBuilder) {

    // Regex mejorada para contraseña: Mín. 8 caracteres, 1 mayúscula, 1 minúscula, 1 número, 1 símbolo.
    const strongPasswordPattern = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
    
    // Regex para nombre: solo letras, espacios, acentos y ñ
    const namePattern = /^[a-zA-ZáéíóúÁÉÍÓÚñÑ\s]+$/;

    this.registerForm = this.fb.group({
      nombre: ['', [Validators.required, Validators.maxLength(100), Validators.pattern(namePattern)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(8), Validators.pattern(strongPasswordPattern)]],
      confirmPassword: ['', [Validators.required]] // Nuevo campo
    }, {
      validator: MustMatchValidator('password', 'confirmPassword') // Validación a nivel de grupo
    });
   }


onSubmit(): void {
  if (this.registerForm.invalid) {
    this.registerForm.markAllAsTouched();
    return;
  }

  this.loading = true;
  this.backendErrors = {};
  this.message = '';

  const { confirmPassword, ...dataToSend } = this.registerForm.value;

  this.authService.register(dataToSend).subscribe({
    next: (response: any) => {
      this.message = response.message || "Registro exitoso";

      // guardamos flag temporal
      localStorage.setItem('just_registered', 'true');

      // pequeña pausa visual más natural
      setTimeout(() => {
        this.loading = false;
        this.router.navigate(['/']); // o '/login' si luego prefieres
      }, 1500);
    },
    error: (err: any) => {
      this.loading = false;

      if (err.status === 400 && typeof err.error === 'object') {
        this.backendErrors = err.error;
      } else {
        this.message = err.error?.message || 'Error en el registro. Vuelve a intentarlo.';
      }
    }
  });
}


  //Metodo de ayuda para acceder facilmente a los controles en la plantilla
  get f(){
    return this.registerForm.controls;
  }
}
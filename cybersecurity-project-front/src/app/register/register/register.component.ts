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

    //Si el formulario es invalido en el cliente, no enviamos la peticion
    if(this.registerForm.invalid){
      this.registerForm.markAllAsTouched();
      return;
    }

    this.backendErrors = {};
    this.message = '';

    // Nota de seguridad: Al enviar el formulario al backend, solo debes incluir los campos necesarios.
    // El 'confirmPassword' es solo para validación del cliente y se debe omitir en el envío.
    const { confirmPassword, ...dataToSend } = this.registerForm.value;


    //enviamos la peticion al backend
    this.authService.register(dataToSend).subscribe({
      next: (response: any) => {
          this.message = response['message'];
          setTimeout(() => this.router.navigate(['/']), 2000);
      },
      error: (err: any) => {
        console.error('Error en el registro:', err);

        //Si el backend devolvio errores de validacion
        if(err.status === 400 && typeof err.error === 'object'){
          this.backendErrors = err.error; //Ej: {email: '....' password: '...'}
        } else{
          this.backendErrors = {};
          this.message = err.error?.['message'] || 'Error en el registro. Vuelve a intentarlo.';
        }

      }

    });
  }

  //Metodo de ayuda para acceder facilmente a los controles en la plantilla
  get f(){
    return this.registerForm.controls;
  }
}
// register.component.ts
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

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

    this.registerForm = this.fb.group({
      nombre: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(4)]]
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

    //enviamos la peticion al backend
    this.authService.register(this.registerForm.value).subscribe({
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
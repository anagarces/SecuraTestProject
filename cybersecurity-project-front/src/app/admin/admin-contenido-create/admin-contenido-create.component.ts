import { Component } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { AdminContenidoService } from '../services/admin-contenido.service';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-admin-contenido-create',
  templateUrl: './admin-contenido-create.component.html'
})
export class AdminContenidoCreateComponent {

  form = this.fb.group({
    titulo: ['', Validators.required],
    cuerpo: ['', Validators.required],
    tema: ['', Validators.required],
    nivel_dificultad: ['', Validators.required]
  });

  constructor(
    private fb: FormBuilder,
    private contenidoService: AdminContenidoService,
    private router: Router,
    private snackBar: MatSnackBar
  ) {}

  save() {
    if (this.form.invalid) {
      this.snackBar.open('Completa todos los campos', 'Cerrar');
      return;
    }

    this.contenidoService.create(this.form.value).subscribe({
      next: () => {
        this.snackBar.open('Contenido creado', 'OK', { duration: 2000 });
        this.router.navigate(['/admin/contenidos']);
      },
      error: () => {
        this.snackBar.open('Error al crear', 'Cerrar');
      }
    });
  }
}

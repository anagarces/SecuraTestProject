import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AdminContenidoService } from '../services/admin-contenido.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { IContenido } from 'src/app/model/contenido';

@Component({
  selector: 'app-admin-contenido-edit',
  templateUrl: './admin-contenido-edit.component.html'
})
export class AdminContenidoEditComponent implements OnInit {

  contenidoId!: number;

  form = this.fb.group({
    titulo: ['', Validators.required],
    cuerpo: ['', Validators.required],
    tema: ['', Validators.required],
    nivelDificultad: ['', Validators.required]
  });

  loading = true;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private contenidoService: AdminContenidoService,
    private router: Router,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.contenidoId = Number(this.route.snapshot.paramMap.get('id'));

    this.contenidoService.getById(this.contenidoId).subscribe({
      next: c => {
        this.form.patchValue({
          titulo: c.titulo,
          cuerpo: c.cuerpo,
          tema: c.tema,
          nivelDificultad: c.nivelDificultad
        });
        this.loading = false;
      },
      error: () => {
        this.snackBar.open('Error cargando contenido', 'Cerrar');
      }
    });
  }
  
  // --- NUEVA FUNCIÓN: CANCELAR/REGRESAR ---
  goBack() {
    this.router.navigate(['/admin/contenidos']);
  }

  save() {
    if (this.form.invalid) {
      this.snackBar.open('Completa todos los campos', 'Cerrar');
      return;
    }

    const contenidoActualizado: IContenido = {
      idContenido: this.contenidoId,
      titulo: this.form.value.titulo!,
      cuerpo: this.form.value.cuerpo!,
      tema: this.form.value.tema!,
      nivelDificultad: this.form.value.nivelDificultad!
    };

    this.contenidoService.update(this.contenidoId, contenidoActualizado).subscribe({
      next: () => {
        this.snackBar.open('Contenido actualizado', 'OK', { duration: 2000 });
        this.router.navigate(['/admin/contenidos']);
      },
      error: () => {
        this.snackBar.open('Error al guardar', 'Cerrar');
      }
    });
  }
}
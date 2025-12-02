import { Component, OnInit } from '@angular/core';
import { AdminContenidoService } from '../services/admin-contenido.service';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-admin-contenido-list',
  templateUrl: './admin-contenido-list.component.html'
})
export class AdminContenidoListComponent implements OnInit {

  contenidos: any[] = [];
  loading = true;

  constructor(
    private contenidoService: AdminContenidoService,
    private router: Router,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.load();
  }

  load() {
    this.contenidoService.getAll().subscribe({
      next: data => {
        this.contenidos = data;
        this.loading = false;
      },
      error: err => {
        this.snackBar.open('Error cargando contenidos', 'Cerrar', { duration: 3000 });
      }
    });
  }

  delete(id: number) {
    if (!confirm('¿Eliminar contenido?')) return;

    this.contenidoService.delete(id).subscribe({
      next: () => {
        this.snackBar.open('Contenido eliminado', 'OK', { duration: 2000 });
        this.load();
      },
      error: () => {
        this.snackBar.open('Error eliminando contenido', 'Cerrar');
      }
    });
  }

  edit(id: number) {
    this.router.navigate(['/admin/contenidos/edit', id]);
  }

  create() {
    this.router.navigate(['/admin/contenidos/create']);
  }
}

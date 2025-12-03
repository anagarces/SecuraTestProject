import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AdminUserService } from '../services/admin-user.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { User } from '../../model/user';

@Component({
  selector: 'app-admin-user-edit',
  templateUrl: './admin-user-edit.component.html',
  styleUrls: ['./admin-user-edit.component.css']
})
export class AdminUserEditComponent implements OnInit {

  userForm!: FormGroup;
  userId!: number;
  loading = true;

  roles = ['USER', 'ADMIN']; // roles disponibles
  user!: User;

  constructor(
    private route: ActivatedRoute,
    private fb: FormBuilder,
    private userService: AdminUserService,
    private snackBar: MatSnackBar,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.userId = Number(this.route.snapshot.paramMap.get('id'));
    this.loadUser();
  }

  // =============================
  // Cargar datos del usuario
  // =============================
  loadUser() {
    this.userService.getById(this.userId).subscribe({
      next: (user) => {
        this.user = user;
        this.buildForm(user);
        this.loading = false;
      },
      error: () => {
        this.snackBar.open('Error cargando usuario', 'Cerrar', { duration: 2000 });
        this.router.navigate(['/admin/users']);
      }
    });
  }

  // =============================
  // Formulario reactivo
  // =============================
  buildForm(user: User) {
    this.userForm = this.fb.group({
      email: [user.email, [Validators.required, Validators.email]],
      nombre: [user.nombre, Validators.required],
      role: [user.role, Validators.required] // SE PUEDE EDITAR AQUÍ TAMBIÉN
    });
  }

  // =============================
  // Guardar cambios
  // =============================
  saveUser() {
    if (this.userForm.invalid) {
      this.snackBar.open('Completa todos los campos correctamente', 'Cerrar', { duration: 2000 });
      return;
    }

    const dto = this.userForm.value;

    this.userService.updateUser(this.userId, dto).subscribe({
      next: () => {
        // Cambio de role si aplica
        if (dto.role !== this.user.role) {
          this.userService.updateRole(this.userId, dto.role).subscribe();
        }

        this.snackBar.open('Usuario actualizado', 'OK', { duration: 2000 });
        this.router.navigate(['/admin/users']);
      },
      error: () => {
        this.snackBar.open('Error guardando usuario', 'Cerrar', { duration: 2000 });
      }
    });
  }

  // =============================
  // Desactivar usuario
  // =============================
  disableUser() {
    if (!confirm('¿Desactivar este usuario?')) return;

    this.userService.disable(this.userId).subscribe({
      next: () => {
        this.snackBar.open('Usuario desactivado', 'OK', { duration: 2000 });
        this.loadUser();
      },
      error: () => {
        this.snackBar.open('Error desactivando usuario', 'Cerrar', { duration: 2000 });
      }
    });
  }

  // =============================
  // Reactivar usuario
  // =============================
  reactivate() {
    if (!confirm('¿Reactivar este usuario?')) return;

    this.userService.reactivate(this.userId).subscribe({
      next: () => {
        this.snackBar.open('Usuario reactivado', 'OK', { duration: 2000 });
        this.loadUser();
      },
      error: () => {
        this.snackBar.open('Error reactivando usuario', 'Cerrar', { duration: 2000 });
      }
    });
  }

  goBack() {
  this.router.navigate(['/admin/users']);
}

}


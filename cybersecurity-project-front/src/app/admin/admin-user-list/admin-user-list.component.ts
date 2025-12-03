import { Component, OnInit } from '@angular/core';
import { AdminUserService } from '../services/admin-user.service';
import { User } from '../../model/user';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';

@Component({
  selector: 'app-admin-user-list',
  templateUrl: './admin-user-list.component.html',
  styleUrls: ['./admin-user-list.component.css']
})
export class AdminUserListComponent implements OnInit {

  users: User[] = [];
  loading = true;

  roles = ['USER', 'ADMIN'];

  constructor(
    private userService: AdminUserService,
    private snackBar: MatSnackBar,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers() {
    this.loading = true;
    this.userService.getAll().subscribe({
      next: (data) => {
        this.users = data;
        this.loading = false;
      },
      error: () => {
        this.snackBar.open('Error cargando usuarios', 'Cerrar', { duration: 3000 });
        this.loading = false;
      }
    });
  }

  changeRole(user: User, event: any) {
    const newRole = event.target.value;

    this.userService.updateRole(user.id, newRole).subscribe({
      next: () => {
        user.role = newRole;
        this.snackBar.open('Rol actualizado correctamente', 'OK', { duration: 2000 });
      },
      error: () => {
        this.snackBar.open('Error actualizando rol', 'Cerrar', { duration: 2000 });
      }
    });
  }

  disableUser(user: User) {
    if (!confirm('¿Desactivar este usuario?')) return;

    this.userService.disable(user.id).subscribe({
      next: () => {
        user.active = false;
        this.snackBar.open('Usuario desactivado', 'OK', { duration: 2000 });
      },
      error: () => {
        this.snackBar.open('Error desactivando usuario', 'Cerrar', { duration: 2000 });
      }
    });
  }

  editUser(user: User) {
    this.router.navigate(['/admin/users', user.id]);
  }
}

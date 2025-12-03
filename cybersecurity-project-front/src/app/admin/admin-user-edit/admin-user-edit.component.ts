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

  userId!: number;
  form!: FormGroup;
  loading = true;

  constructor(
    private route: ActivatedRoute,
    private fb: FormBuilder,
    private userService: AdminUserService,
    private snackBar: MatSnackBar,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.userId = Number(this.route.snapshot.paramMap.get('id'));

    this.form = this.fb.group({
      nombre: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]]
    });

    this.loadUser();
  }

  loadUser() {
    this.userService.getById(this.userId).subscribe({
      next: (user: User) => {
        this.form.patchValue({
          nombre: user.nombre,
          email: user.email
        });
        this.loading = false;
      },
      error: () => {
        this.snackBar.open('Error cargando usuario', 'Cerrar', { duration: 3000 });
        this.router.navigate(['/admin/users']);
      }
    });
  }

  save() {
    if (this.form.invalid) {
      this.snackBar.open('Completa todos los campos', 'Cerrar', { duration: 3000 });
      return;
    }

    this.userService.updateUser(this.userId, this.form.value).subscribe({
      next: () => {
        this.snackBar.open('Usuario actualizado', 'OK', { duration: 2000 });
        this.router.navigate(['/admin/users']);
      },
      error: () => {
        this.snackBar.open('Error actualizando usuario', 'Cerrar', { duration: 2000 });
      }
    });
  }
}

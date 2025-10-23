import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ContentComponent } from './contenido/content.component';
import { HomeComponent } from './inicio/home.component';
import { ContentDetailComponent } from './contenido/content-detail/content-detail.component';
import { LoginComponent } from './login/login/login.component';
import { AuthGuard } from './guards/auth.guard';
import { RegisterComponent } from './register/register/register.component';

const routes: Routes = [


  // Es la primera página que verá un usuario.
  { path: '', component: LoginComponent },
   { path: 'register', component: RegisterComponent },
    
  {// Nadie puede acceder a ella ni a sus sub-rutas sin haber iniciado sesión.
    path: 'contenido',
    canActivate: [AuthGuard], // <-- El portero protege esta ruta
    component: ContentComponent
  },

  {// 4. La ruta para el detalle de un contenido también está protegida.
    path: 'contenido/:id',
    canActivate: [AuthGuard], // <-- El portero también protege esta
    component: ContentDetailComponent
  },
   // 5. (Opcional pero recomendado) Redirige cualquier ruta no encontrada a la página de inicio.
  // Esta debe ser SIEMPRE la última ruta de la lista.
  { path: '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})


export class AppRoutingModule { }

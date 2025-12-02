import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ContentComponent } from './contenido/content.component';
import { HomeComponent } from './inicio/home.component';
import { ContentDetailComponent } from './contenido/content-detail/content-detail.component';
import { LoginComponent } from './login/login/login.component';
import { AuthGuard } from './guards/auth.guard';
import { RegisterComponent } from './register/register/register.component';
import { QuizListComponent } from './cuestionarios/quiz-list/quiz-list.component';
import { QuizTakeComponent } from './cuestionarios/quiz-take/quiz-take.component';
import { MisResultadosComponent } from './pages/mis-resultados/mis-resultados.component';
import { AdminQuizListComponent } from './admin/admin-quiz-list/admin-quiz-list.component';
import { AdminQuizCreateComponent } from './admin/admin-quiz-create/admin-quiz-create.component';
import { AdminQuizEditComponent } from './admin/admin-quiz-edit/admin-quiz-edit.component';
import { AdminResultsComponent } from './admin/admin-results/admin-results.component';
import { AdminGuard } from './guards/admin.guard';
import { AdminContenidoListComponent } from './admin/admin-contenido-list/admin-contenido-list.component';
import { AdminContenidoCreateComponent } from './admin/admin-contenido-create/admin-contenido-create.component';
import { AdminContenidoEditComponent } from './admin/admin-contenido-edit/admin-contenido-edit.component';

const routes: Routes = [


  //Pagina principal
{ path: '', component: HomeComponent},

//Formulario de inicio de sesion
{path: 'login', component: LoginComponent},

  //Formulario de registro
   { path: 'register', component: RegisterComponent },
    
  {// Nadie puede acceder a ella ni a sus sub-rutas sin haber iniciado sesión.
    path: 'contenido',
    canActivate: [AuthGuard], // El portero protege esta ruta
    component: ContentComponent
  },

  {// 4. La ruta para el detalle de un contenido también está protegida.
    path: 'contenido/:id',
    canActivate: [AuthGuard], // <-- El portero también protege esta
    component: ContentDetailComponent
  },

  {
    path: 'quizzes',
    canActivate: [AuthGuard],
    component: QuizListComponent
  },

  //Muestra un cuestionario
  {
    path: 'quiz/:id',
    canActivate: [AuthGuard],
    component: QuizTakeComponent
  },

  { path: 'mis-resultados', canActivate: [AuthGuard], component: MisResultadosComponent },

  {
  path: 'admin/quizzes',
  component: AdminQuizListComponent,
  canActivate: [AdminGuard]
},
{
  path: 'admin/quizzes/create',
  component: AdminQuizCreateComponent,
  canActivate: [AdminGuard]
},
{
  path: 'admin/quizzes/edit/:id',
  component: AdminQuizEditComponent,
  canActivate: [AdminGuard]
},
{
  path: 'admin/results',
  component: AdminResultsComponent,
  canActivate: [AdminGuard]
},

{
  path: 'admin/contenidos',
  canActivate: [AdminGuard],
  children: [
    { path: '', component: AdminContenidoListComponent },
    { path: 'create', component: AdminContenidoCreateComponent },
    { path: 'edit/:id', component: AdminContenidoEditComponent }
  ]
},



   // 5. Redirige cualquier ruta no encontrada a la página de inicio.
  // Esta debe ser SIEMPRE la última ruta de la lista.
  { path: '**', redirectTo: '' },

  //Lista de cuestionarios

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})


export class AppRoutingModule { }

import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { HomeComponent } from './inicio/home.component';
import { LoginComponent } from './login/login/login.component';
import { RegisterComponent } from './register/register/register.component';

import { ContentComponent } from './contenido/content.component';
import { ContentDetailComponent } from './contenido/content-detail/content-detail.component';

import { QuizListComponent } from './cuestionarios/quiz-list/quiz-list.component';
import { QuizTakeComponent } from './cuestionarios/quiz-take/quiz-take.component';

import { MisResultadosComponent } from './pages/mis-resultados/mis-resultados.component';

import { AdminQuizListComponent } from './admin/admin-quiz-list/admin-quiz-list.component';
import { AdminQuizCreateComponent } from './admin/admin-quiz-create/admin-quiz-create.component';
import { AdminQuizEditComponent } from './admin/admin-quiz-edit/admin-quiz-edit.component';

import { AdminResultsComponent } from './admin/admin-results/admin-results.component';

import { AdminContenidoListComponent } from './admin/admin-contenido-list/admin-contenido-list.component';
import { AdminContenidoCreateComponent } from './admin/admin-contenido-create/admin-contenido-create.component';
import { AdminContenidoEditComponent } from './admin/admin-contenido-edit/admin-contenido-edit.component';

import { AdminUserListComponent } from './admin/admin-user-list/admin-user-list.component';
import { AdminUserEditComponent } from './admin/admin-user-edit/admin-user-edit.component';

import { AuthGuard } from './guards/auth.guard';
import { AdminGuard } from './guards/admin.guard';

const routes: Routes = [

  // ======================
  // PÚBLICAS
  // ======================
  { path: '', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },

  // ======================
  // CONTENIDO (PROTEGIDO)
  // ======================
  {
    path: 'contenido',
    component: ContentComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'contenido/:id',
    component: ContentDetailComponent,
    canActivate: [AuthGuard]
  },

  // ======================
  // QUIZZES (PROTEGIDO)
  // ======================
  {
    path: 'quizzes',
    component: QuizListComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'quiz/:id',
    component: QuizTakeComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'mis-resultados',
    component: MisResultadosComponent,
    canActivate: [AuthGuard]
  },

  // ======================
  // ADMIN QUIZZES
  // ======================
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

  // ======================
  // ADMIN RESULTADOS
  // ======================
  {
    path: 'admin/results',
    component: AdminResultsComponent,
    canActivate: [AdminGuard]
  },

  // ======================
  // ADMIN CONTENIDOS
  // ======================
  {
    path: 'admin/contenidos',
    canActivate: [AdminGuard],
    children: [
      { path: '', component: AdminContenidoListComponent },
      { path: 'create', component: AdminContenidoCreateComponent },
      { path: 'edit/:id', component: AdminContenidoEditComponent }
    ]
  },

  // ======================
  // ADMIN USUARIOS
  // ======================
  {
    path: 'admin/users',
    component: AdminUserListComponent,
    canActivate: [AdminGuard]
  },
  {
    path: 'admin/users/:id',
    component: AdminUserEditComponent,
    canActivate: [AdminGuard]
  },

  // ======================
  // RUTA NO ENCONTRADA
  // ======================
  { path: '**', redirectTo: '' }

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}

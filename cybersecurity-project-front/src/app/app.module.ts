import { AppComponent } from './app.component';
import { AppRoutingModule } from './app-routing.module';

import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';


//librerias interfaz
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBarModule } from '@angular/material/snack-bar';

//Componentes de la app
import { ContentComponent } from './contenido/content.component';
import { HomeComponent } from './inicio/home.component';

//peticiones
import { HttpClientModule } from '@angular/common/http';
import { ContentDetailComponent } from './contenido/content-detail/content-detail.component';
import { LoginComponent } from './login/login/login.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RegisterComponent } from './register/register/register.component';

import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { JwtInterceptor } from './services/jwt.interceptor';
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


@NgModule({
  declarations: [
    AppComponent,
    ContentComponent,
    HomeComponent,
    ContentDetailComponent,
    LoginComponent,
    RegisterComponent,
    QuizListComponent,
    QuizTakeComponent,
    MisResultadosComponent,
    AdminQuizListComponent,
    AdminQuizCreateComponent,
    AdminQuizEditComponent,
    AdminResultsComponent,
    AdminContenidoListComponent,
    AdminContenidoCreateComponent,
    AdminContenidoEditComponent,
    AdminUserListComponent,
    AdminUserEditComponent,
  
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    BrowserAnimationsModule, 
    FormsModule,
    HttpClientModule,
    MatToolbarModule,
    MatButtonModule,
    MatIconModule,
    MatSnackBarModule,
    ReactiveFormsModule
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }

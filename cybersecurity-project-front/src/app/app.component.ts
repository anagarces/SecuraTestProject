import { Component } from '@angular/core';
import { AuthService } from './services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'cybersecurity-project';

   constructor(public authService: AuthService, private router: Router) {}

   //cerrar sesion
     logout(): void {

      //llama al metodo del servicio para limpiar el token
    this.authService.logout();

    //redirige al usuario al login/inicio
      this.router.navigate(['/']);
  }

}

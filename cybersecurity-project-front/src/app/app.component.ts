import { Component } from '@angular/core';
import { AuthService } from './services/auth.service';
import { Router } from '@angular/router';
import {jwtDecode} from 'jwt-decode';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'cybersecurity-project';
  adminMenuOpen = false;

   constructor(public authService: AuthService, private router: Router) {
    (window as any).jwtDecode = jwtDecode;
}
 


   //cerrar sesion
 logout(): void {
  this.authService.logout(); // ← esto YA limpia todo y navega
}


toggleAdminMenu() {
  this.adminMenuOpen = !this.adminMenuOpen;
}


}

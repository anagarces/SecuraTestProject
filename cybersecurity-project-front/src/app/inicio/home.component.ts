import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-home', // o dashboard según tu ruta
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  showWelcomeModal = false;

  ngOnInit() {
    const justRegistered = localStorage.getItem('just_registered');
    if (justRegistered) {
      this.showWelcomeModal = true;
      localStorage.removeItem('just_registered');
    }
  }

  closeModal() {
    this.showWelcomeModal = false;
  }

  goToLogin() {
    window.location.href = '/login';
  }
}

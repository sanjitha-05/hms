import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-home',
  standalone: false,
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent {
  constructor(private router: Router) {}

  onLogin() {
    // Navigate to the login page (update the route as per your app)
    this.router.navigate(['/login']);
  }

  onRegister() {
    // Navigate to the register page (update the route as per your app)
    this.router.navigate(['/register']);
  }
}

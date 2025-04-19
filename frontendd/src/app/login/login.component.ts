import { Component } from '@angular/core';
import axios from 'axios';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone:false,
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  // Form data
  patientId: number | null = null;
  name: string = '';
  dateOfBirth: string = '';
  contactDetails: string = '';
  constructor(private router: Router) {}

  // Handle form submission
  async onLogin() {
    const loginData = {
      patientId: this.patientId,
      name: this.name,
      dateOfBirth: this.dateOfBirth,
      contactDetails: this.contactDetails
    };

    try {
      console.log('Login request starting...');
      const response = await axios.post('http://localhost:8080/api/hospital/patients/save', loginData);
      console.log('Login request complete:', response.data);
      alert('Login successful!');
      this.router.navigate(['/patient']);
    } catch (error) {
      console.error('Error during login:', error);
      alert('Login failed. Please check your details and try again.');
    }
  }
}
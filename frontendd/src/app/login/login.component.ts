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
  
  name: string = '';
  dateOfBirth: string = '';
  contactDetails: string = '';
  constructor(private router: Router) {}

  // Handle form submission
  async onLogin() {


    const patientId = localStorage.getItem('userId');
    if (!patientId) {
      alert('Patient ID not found in localStorage. Please log in again.');
      return;
    }
    const loginData = {
      patientId: parseInt(patientId, 10),
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
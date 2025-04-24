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

  isFutureDate(dateString: string): boolean {
    if (!dateString) {
        return false; // Consider empty date as valid or invalid based on your requirement
    }
    const selectedDate = new Date(dateString);
    const today = new Date();
    today.setHours(0, 0, 0, 0); // Compare only the date part
    return selectedDate > today;
}
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
      const token=localStorage.getItem('token')
      const response = await axios.post('http://localhost:8080/api/hospital/patients/save', loginData,
        {
          headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json',
          },
        }
      );
     // console.log('Login request complete:', response.data);
      alert('Login successful!');
      this.router.navigate(['/patient']);
    } catch (error) {
      console.error('Error during login:', error);
      alert('Login failed. Please check your details and try again.');
    }
  }
}
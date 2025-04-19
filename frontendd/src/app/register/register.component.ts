import { Component } from '@angular/core';
import axios from 'axios';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  standalone: false,
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {

  email: string = '';
  password: string = '';

  constructor(private router: Router) {}
  async onRegister() {
    const registerData = {
      email: this.email,
      password: this.password
    };

    if (this.email.includes('@doctor.com')) {
      try {
        // Check if the doctor exists
        const checkResponse = await axios.post('http://localhost:8080/api/register/check', registerData);
        if (checkResponse.status === 200) {
          // Store token and doctorId in localStorage
          const token = checkResponse.data.token; // Get the token from the response
          const doctorId = checkResponse.data.userId; // Get the doctorId from the response
          localStorage.setItem('token', token); // Store the token in localStorage
          localStorage.setItem('doctorId', doctorId.toString()); // Store the doctorId in localStorage

          // Redirect to Doctor Dashboard
          alert('Redirecting to Doctor Dashboard...');
          this.router.navigate(['/doctor']);
          return;
        }
      } catch (error) {
        console.error('Error during doctor login:', error);
        alert('Failed to log in as a doctor. Please check your credentials.');
        return;
      }
    }

    try {
      // Check if the user already exists
      const checkResponse = await axios.post('http://localhost:8080/api/register/check', registerData);
      if (checkResponse.status === 200) {
        console.log("hiiii")
        const token = checkResponse.data.token; // Get the token from the response
        const userId = checkResponse.data.userId; // Get the userId from the response
        localStorage.setItem('token', token); // Store the token in localStorage
        localStorage.setItem('userId', userId.toString()); 
        // User exists, navigate to Patient Dashboard
        alert('User already exists. Redirecting to Patient Dashboard...');
        this.router.navigate(['/patient']);
        return;
      }
    } catch (error) {

      try {
        console.log("register starting")
        const response = await axios.post('http://localhost:8080/api/register/', registerData);
        console.log("register complete")

        const token = response.data.token; // Assuming the backend returns a token
        const userId = response.data.userId; // Assuming the backend returns a userId
        localStorage.setItem('token', token); // Store the token in localStorage
        localStorage.setItem('userId', userId.toString()); // Store the userId in localStorage
       // localStorage.setItem('email', this.email);

        alert('Registration successful!');
        console.log('Response:', response.data);
        this.router.navigate(['/login']);
      } catch (error) {
        console.error('Error registering user:', error);
        // if (error.response && error.response.data) {
        //   alert(`Error: ${error.response.data}`);
        // } else {
          alert('Failed to registerrrr. Please try again.');
        //}
      }
      
      // if (error.response && error.response.status === 404) {
      //   console.log('User not found. Proceeding with registration...');
      // } else {
        console.error('Error checking user:', error);

        //alert('An error occurreddddddddddddd. Please try again.');
        return;
      //}
    }

    
  }

}

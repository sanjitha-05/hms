import { Component } from '@angular/core';
import axios from 'axios';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr'; 

@Component({
  selector: 'app-register',
  standalone: false,
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {

  email: string = '';
  password: string = '';

  constructor(private router: Router, private toastr: ToastrService) {}
  async onRegister() {
    const registerData = {
      email: this.email,
      password: this.password
    };

    if (this.email.includes('@doctor.com')) {
      try {
        const checkResponse = await axios.post('http://localhost:8080/api/register/check', registerData);
        if (checkResponse.status === 200) {
          
          const token = checkResponse.data.token; 
          const doctorId = checkResponse.data.userId; 
          localStorage.setItem('token', token); 
          localStorage.setItem('doctorId', doctorId.toString()); 

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
      
      const checkResponse = await axios.post('http://localhost:8080/api/register/check', registerData);
      if (checkResponse.status === 200) {
        const token = checkResponse.data.token;
        const userId = checkResponse.data.userId;
        localStorage.setItem('token', token);
        localStorage.setItem('userId',userId.toString());
        this.toastr.info('User already exists. Redirecting to Patient Dashboard...', 'Info');
        this.router.navigate(['/patient']);
        return;
      }
    } catch (error) {
      try {
        
        const response = await axios.post('http://localhost:8080/api/register/', registerData);
       

        const token = response.data.token; 
        const userId = response.data.userId; 
        localStorage.setItem('token', token);
        localStorage.setItem('userId', userId.toString());
      
        this.toastr.success('Registration successful!', 'Success');
        this.router.navigate(['/login']);
      } catch (error) {
        console.error('Error registering user:', error);
          alert('Failed to registerrrr. Please try again.');
        
      }
        console.error('Error checking user:', error);

    }

    
  }

}

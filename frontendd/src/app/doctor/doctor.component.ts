import { Component } from '@angular/core';
import axios from 'axios';
import { Router } from '@angular/router';

@Component({
  selector: 'app-doctor',
  standalone:false,
  templateUrl: './doctor.component.html',
  styleUrls: ['./doctor.component.css']
})
export class DoctorComponent {
  doctorId: number | null = null;
  upcomingAppointments: any[] = [];
  notifications: any[] = []; 
  doctorNotifications: any[] = [];

  constructor(private router: Router) {
    const storedDoctorId = localStorage.getItem('doctorId');
    if (storedDoctorId) {
      this.doctorId = parseInt(storedDoctorId, 10);
    }
  }
  

  async createAvailability() {
    if (!this.doctorId) {
      alert('Doctor ID not found. Please log in again.');
      return;
    }

    try {
      const token=localStorage.getItem('token')
      const response = await axios.put(`http://localhost:8080/api/hospital/doctors/create/${this.doctorId}`,{},
        {
          headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json',
          },
        }
      );
      alert('Availability for the next 7 days has been created successfully!');
    } catch (error) {
      alert('Failed to create availability. Please try again later.');
    }
  }

    navigateToAppointments() {
      this.router.navigate(['/doctor-appointments']);
    }
        onLogout() {
          localStorage.clear();
          this.router.navigate(['/register']);
        }
        onViewProfile() {
          this.router.navigate(['/profile']);
        }
        onNotifications() {
          if (!this.doctorId) {
            alert('Doctor ID not found. Please log in again.');
            return;
          }
      
          this.router.navigate(['/notifications'], {
            queryParams: { userType: 'doctor', doctorId: this.doctorId }
          });
        }
        navigateToManageAvailability() {
          this.router.navigate(['/manage-availability']);
        }

}
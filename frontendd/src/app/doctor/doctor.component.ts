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
      const response = await axios.put(`http://localhost:8080/api/hospital/doctors/create/${this.doctorId}`);
      alert('Availability for the next 7 days has been created successfully!');
      console.log('Response:', response.data);
    } catch (error) {
      console.error('Error creating availability:', error);
      alert('Failed to create availability. Please try again later.');
    }
  }

    navigateToAppointments() {
      this.router.navigate(['/doctor-appointments']);
    }

    async fetchNotifications() {
          if (!this.doctorId) {
            alert('Doctor ID not found. Please log in again.');
            return;
          }
      
          try {
            const response = await axios.get(`http://localhost:8080/api/hospital/notifications/doctor/${this.doctorId}`);
            
            this.notifications = response.data;
      
            this.doctorNotifications = this.notifications.filter((notification: any) =>
              notification.message.startsWith('Hi')
            );          
          } catch (error) {
            console.error('Error fetching notifications:', error);
            alert('Failed to fetch notifications. Please try again later.');
          }
        }
  

        viewNotifications() {
          if (!this.doctorId) {
            alert('Doctor ID not found. Please log in again.');
            return;
          }
        
          this.router.navigate(['/notifications'], {
            queryParams: { userType: 'doctor', doctorId: this.doctorId }
          });
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
      

  managePrescriptions() {
    alert('Navigating to the Prescriptions page...');
  }
}
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import axios from 'axios';


@Component({
  selector: 'app-patient',
  standalone:false,
  templateUrl: './patient.component.html',
  styleUrls: ['./patient.component.css']
})
export class PatientComponent {
  patientName: string = '';

  constructor(private router: Router) {}

  ngOnInit(): void {
    this.fetchPatientDetails();
  }

  // Fetch patient details from the backend
  async fetchPatientDetails() {
    const patientId = localStorage.getItem('userId'); // Retrieve the patient ID from localStorage
    if (!patientId) {
      alert('User not logged in. Please log in again.');
      this.router.navigate(['/login']);
      return;
    }
    try {
      const token=localStorage.getItem('token');
      const response = await axios.get(`http://localhost:8080/api/hospital/patients/get/${patientId}`,
        {
          headers: {
            Authorization: `Bearer ${token}`, 
            'Content-Type': 'application/json',
          },
        }
      );
      this.patientName = response.data.name;
    } catch (error) {
      console.error('Error fetching patient details:', error);
      alert('Failed to fetch patient details. Please try again later.');
    }
  }

  

  onBookAppointment() {
    this.router.navigate(['/book-appointment']);
  }

  onViewMedicalHistory() {
    this.router.navigate(['/medical-history']);
  }

  onViewAppointments() {
    this.router.navigate(['/my-appointments']);
  }
  onViewProfile() {
    this.router.navigate(['/profile']);
  }

  onLogout() {
    localStorage.clear();
    this.router.navigate(['/register']);
  }

  onNotifications() {
    const userId = localStorage.getItem('userId');
    if (!userId) {
      alert('User ID not found. Please log in again.');
      return;
    }
  
    console.log('Navigating to notifications with queryParams:', { userType: 'patient', userId: userId });
    this.router.navigate(['/notifications'], {
      queryParams: { userType: 'patient', userId: userId }
    });
  }

  onAbout() {
    alert('About: This is a Hospital Management System.');
  }

  onContact() {
    alert('Contact Us: Email us at support@hospital.com or call +1-800-123-4567.');
  }
}

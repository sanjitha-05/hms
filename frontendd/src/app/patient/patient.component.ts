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
      // Call the backend API to fetch patient details
      const response = await axios.get(`http://localhost:8080/api/hospital/patients/${patientId}`);
      this.patientName = response.data.name; // Assign the patient's name
    } catch (error) {
      console.error('Error fetching patient details:', error);
      alert('Failed to fetch patient details. Please try again later.');
    }
  }

  

  onBookAppointment() {
    // Navigate to the book appointment page
    this.router.navigate(['/book-appointment']);
  }

  onViewMedicalHistory() {
    // Navigate to the medical history page
    this.router.navigate(['/medical-history']);
  }

  onViewAppointments() {
    // Navigate to the My Appointments page
    this.router.navigate(['/my-appointments']);
  }

  onLogout() {
    // Navigate to the login page
    this.router.navigate(['/login']);
  }

  onNotifications() {
    // Navigate to the notifications page
    this.router.navigate(['/notifications']);
  }

  onAbout() {
    // Navigate to the about page (or display a modal)
    alert('About: This is a Hospital Management System.');
  }

  onContact() {
    // Navigate to the contact page (or display a modal)
    alert('Contact Us: Email us at support@hospital.com or call +1-800-123-4567.');
  }
}

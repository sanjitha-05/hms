import { Component, OnInit } from '@angular/core';
import { PatientService } from '../services/patient.service';

@Component({
  selector: 'app-profile',
  standalone:false,
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  patientDetails: any = {}; // Object to store patient details
  errorMessage: string = ''; // Error message for handling errors
  email: string = ''; // Email address from User entity
  password: string = '';

  constructor(private patientService: PatientService) {}

  ngOnInit(): void {
    const storedPatientId = localStorage.getItem('userId'); // Retrieve patient ID from localStorage
    if (storedPatientId) {
      const patientId = parseInt(storedPatientId, 10); // Convert the ID to a number
      this.fetchPatientDetails(patientId); // Fetch patient details using the retrieved ID
    } else {
      console.error('No patient ID found in localStorage.');
      this.errorMessage = 'Failed to load patient details. Please log in again.';
    }
  }

  // Fetch patient details from the backend
  async fetchPatientDetails(patientId: number) {
    try {
      const data = await this.patientService.getPatientDetails(patientId);
      this.patientDetails = await this.patientService.getPatientDetails(patientId);
      this.patientDetails = data;
      this.email = data.email; // Extract email
      this.password = data.password;
    } catch (error) {
      console.error('Error fetching patient details:', error);
      this.errorMessage = 'Failed to load patient details. Please try again later.';
    }
  }
}
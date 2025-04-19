import { Component, OnInit } from '@angular/core';
import axios from 'axios';

@Component({
  selector: 'app-medical-history',
  standalone:false,
  templateUrl: './medical-history.component.html',
  styleUrls: ['./medical-history.component.css']
})
export class MedicalHistoryComponent {
  medicalHistory: any[] = []; // To store medical history records
  patientId: string | null = null;


  constructor() {}

  ngOnInit(): void {
    // Retrieve the patient ID from localStorage
    this.patientId = localStorage.getItem('userId');

    if (this.patientId) {
      this.fetchMedicalHistory();
    } else {
      alert('Patient ID not found in localStorage. Please log in again.');
    }
  }
  async fetchMedicalHistory() {
    try {
      const response = await axios.get(`http://localhost:8080/api/hospital/history/view/${this.patientId}`);
      this.medicalHistory = response.data; // Assign the fetched data to the array
    } catch (error) {
      console.error('Error fetching medical history:', error);
      alert('Failed to fetch medical history. Please try again later.');
    }
  }
}
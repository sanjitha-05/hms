import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import axios from 'axios'
@Component({
  selector: 'app-prescription',
  standalone:false,
  templateUrl: './prescription.component.html',
  styleUrls: ['./prescription.component.css']
})
export class PrescriptionComponent {
  // Fields for entering prescription details
  patientId: string = '';
  diagnosis: string = '';
  treatment: string = '';
  dateOfVisit: string = '';

  // Array to store prescriptions
  prescriptions: any[] = [];

  constructor() {}

  // Method to add a new prescription
  async addPrescription() {
    if (!this.patientId || !this.diagnosis || !this.treatment || !this.dateOfVisit) {
      alert('Please fill in all fields to add a prescription.');
      return;
    }
    const formattedDateOfVisit = `${this.dateOfVisit}T00:00:00`;

    const newPrescription = {
      patient: { patientId: this.patientId }, // Backend expects a PatientProfile object
      diagnosis: this.diagnosis,
      treatment: this.treatment,
      dateOfVisit: formattedDateOfVisit
    };


    try {
      // Send the prescription to the backend
      const response = await axios.post('http://localhost:8080/api/hospital/history/save', newPrescription);
      alert('Prescription added successfully!');
      this.prescriptions.push(response.data); // Add the response to the local array
      this.clearForm();
    } catch (error) {
      console.error('Error adding prescription:', error);
      alert('Failed to add prescription. Please try again.');
    }
}
clearForm() {
  this.patientId = '';
  this.diagnosis = '';
  this.treatment = '';
  this.dateOfVisit = '';
}
}
   

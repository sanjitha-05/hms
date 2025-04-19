import { Component } from '@angular/core';

@Component({
  selector: 'app-medical-history',
  standalone:false,
  templateUrl: './medical-history.component.html',
  styleUrls: ['./medical-history.component.css']
})
export class MedicalHistoryComponent {
  medicalHistory: any[] = []; // To store medical history records

  // Fields for adding medical history
  patientName: string = '';
  diagnosis: string = '';
  treatment: string = '';
  dateOfVisit: string = '';

  constructor() {
    this.loadStaticMedicalHistory(); // Load static medical history
  }

  // Load static medical history
  loadStaticMedicalHistory() {
    this.medicalHistory = [
      {
        dateOfVisit: '2023-09-15',
        patientName: 'John Doe',
        diagnosis: 'Flu',
        treatment: 'Paracetamol, Rest, Hydration'
      },
      {
        dateOfVisit: '2023-08-10',
        patientName: 'Jane Smith',
        diagnosis: 'Back Pain',
        treatment: 'Ibuprofen, Physical Therapy'
      }
    ];
  }

  // Add a new medical history record
  addMedicalHistory() {
    if (!this.patientName || !this.diagnosis || !this.treatment || !this.dateOfVisit) {
      alert('Please fill in all fields to add medical history.');
      return;
    }

    const newHistory = {
      dateOfVisit: this.dateOfVisit,
      patientName: this.patientName,
      diagnosis: this.diagnosis,
      treatment: this.treatment
    };

    this.medicalHistory.push(newHistory); // Add the new record to the array
    alert('Medical history added successfully!');

    // Clear the form fields
    this.patientName = '';
    this.diagnosis = '';
    this.treatment = '';
    this.dateOfVisit = '';
  }
}
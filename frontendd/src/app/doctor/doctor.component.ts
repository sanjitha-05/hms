import { Component } from '@angular/core';
import axios from 'axios';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-doctor',
  standalone:false,
  templateUrl: './doctor.component.html',
  styleUrls: ['./doctor.component.css']
})
export class DoctorComponent {
  doctorId: number | null = null;
  upcomingAppointments: any[] = [];
 

  

  constructor() {
    // Retrieve the doctor ID from localStorage or sessionStorage (if applicable)
    const storedDoctorId = localStorage.getItem('doctorId');
    if (storedDoctorId) {
      this.doctorId = parseInt(storedDoctorId, 10);
    }
    this.fetchAppointments();
   
  }

  async fetchAppointments() {
    if (!this.doctorId) {
      alert('Doctor ID not found. Please log in again.');
      return;
    }



    try {
      const response = await axios.get(`http://localhost:8080/api/hospital/appointments/getbydoctor/${this.doctorId}`);
      console.log(response.data)
      this.upcomingAppointments = response.data.map((appointment: any) => ({
        date: appointment.doctor.date, // Extract the date from the doctor object
        time: appointment.appointmentTime, // Extract the appointment time
        patientName: appointment.patient.name, // Extract the patient name
        status: appointment.status // Extract the appointment status
      })); // Assign fetched appointments to the array
    } catch (error) {
      console.error('Error fetching appointments:', error);
      alert('Failed to fetch appointments. Please try again later.');
    }
  }
  
  
  // loadStaticMedicalHistory() {
  //   this.medicalHistory = [
  //     {
  //       dateOfVisit: '2023-09-15',
  //       patientName: 'John Doe',
  //       diagnosis: 'Flu',
  //       treatment: 'Paracetamol, Rest, Hydration'
  //     },
  //     {
  //       dateOfVisit: '2023-08-10',
  //       patientName: 'Jane Smith',
  //       diagnosis: 'Back Pain',
  //       treatment: 'Ibuprofen, Physical Therapy'
  //     }
  //   ];
  // }

  // addMedicalHistory() {
  //   if (!this.patientName || !this.diagnosis || !this.treatment || !this.dateOfVisit) {
  //     alert('Please fill in all fields to add medical history.');
  //     return;
  //   }

  //   const newHistory = {
  //     dateOfVisit: this.dateOfVisit,
  //     patientName: this.patientName,
  //     diagnosis: this.diagnosis,
  //     treatment: this.treatment
  //   };

  //   this.medicalHistory.push(newHistory); // Add the new record to the array
  //   alert('Medical history added successfully!');

  //   // Clear the form fields
  //   this.patientName = '';
  //   this.diagnosis = '';
  //   this.treatment = '';
  //   this.dateOfVisit = '';
  // }


  // addPrescription() {
  //   if (!this.patientName || !this.diagnosis || !this.treatment) {
  //     alert('Please fill in all fields to add a prescription.');
  //     return;
  //   }

  //   const newPrescription = {
  //     patientName: this.patientName,
  //     diagnosis: this.diagnosis,
  //     treatment: this.treatment
  //   };

  //   this.prescriptions.push(newPrescription); // Add the new prescription to the array
  //   alert('Prescription added successfully!');

  //   // Clear the form fields
  //   this.patientName = '';
  //   this.diagnosis = '';
  //   this.treatment = '';
  // }

  // blockedDates: string[] = []; // To store blocked dates

  // // Simulate creating availability for the next 7 days
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
  // Simulate blocking availability for a specific date
  // blockAvailability(date: string) {
  //   if (!this.blockedDates.includes(date)) {
  //     this.blockedDates.push(date);
  //     alert(`Availability for ${date} has been blocked.`);
  //   } else {
  //     alert(`Availability for ${date} is already blocked.`);
  //   }
  // }

  // // Simulate unblocking availability for a specific date
  // unblockAvailability(date: string) {
  //   const index = this.blockedDates.indexOf(date);
  //   if (index > -1) {
  //     this.blockedDates.splice(index, 1);
  //     alert(`Availability for ${date} has been unblocked.`);
  //   } else {
  //     alert(`Availability for ${date} is not blocked.`);
  //   }
  // }
}
import { Component } from '@angular/core';
import axios from 'axios';

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

  constructor() {
    // Retrieve the doctor ID from localStorage or sessionStorage (if applicable)
    const storedDoctorId = localStorage.getItem('doctorId');
    if (storedDoctorId) {
      this.doctorId = parseInt(storedDoctorId, 10);
    }
    this.fetchAppointments();
    this.fetchNotifications();
   
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

  async fetchNotifications() {
    if (!this.doctorId) {
      alert('Doctor ID not found. Please log in again.');
      return;
    }

    try {
      // Call the backend API to fetch notifications for the doctor
      const response = await axios.get(`http://localhost:8080/api/hospital/notifications/doctor/${this.doctorId}`);
      console.log('Doctor Notifications:', response.data);
      this.notifications = response.data; // Assign the fetched notifications to the array
      this.doctorNotifications = this.notifications.filter((notification: any) =>
        notification.message.startsWith('Hi')
      );
    
    } catch (error) {
      console.error('Error fetching notifications:', error);
      alert('Failed to fetch notifications. Please try again later.');
    }
  }

  
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
import { Component, OnInit } from '@angular/core';
import axios from 'axios';

@Component({
  selector: 'app-book-appointment',
  standalone:false,
  templateUrl: './book-appointment.component.html',
  styleUrls: ['./book-appointment.component.css']
})
export class BookAppointmentComponent implements OnInit {
  doctors: any[] = []; // List of doctors fetched from the backend
  availableDates: string[] = []; // Dates fetched based on selected doctor
  availableTimeSlots: any[] = []; // Time slots fetched based on selected date

  selectedDoctorId: number | null = null; // Selected doctor's ID
  selectedDate: string = ''; // Selected date
  selectedTime: string = ''; // Selected time slot

  ngOnInit() {
    this.fetchDoctors();
  }

  async fetchDoctors() {
    try {
      const response = await axios.get('http://localhost:8080/api/hospital/doctors');
      const allDoctors = response.data; // Assuming the backend returns a list of doctor objects
  
      // Use a Set to filter out duplicate doctor names
      const uniqueDoctorsMap = new Map();
      allDoctors.forEach((doctor: any) => {
        if (!uniqueDoctorsMap.has(doctor.doctorId)) {
          uniqueDoctorsMap.set(doctor.doctorId, doctor);
        }
      });
  
      this.doctors = Array.from(uniqueDoctorsMap.values()); // Convert the Map back to an array
    } catch (error) {
      console.error('Error fetching doctors:', error);
      alert('Failed to fetch doctors. Please try again later.');
    }
  }

  // Fetch available dates for the selected doctor
  async onDoctorChange() {
    if (!this.selectedDoctorId) return;

    try {
      const response = await axios.get(`http://localhost:8080/api/hospital/doctors/${this.selectedDoctorId}/available-dates`);
      this.availableDates = response.data; // Assuming the backend returns a list of dates
      this.availableTimeSlots = []; // Reset time slots when doctor changes
      this.selectedDate = ''; // Reset selected date
    } catch (error) {
      console.error('Error fetching available dates:', error);
      alert('Failed to fetch available dates. Please try again later.');
    }
  }

  // Fetch available time slots for the selected date
  async onDateChange() {
    if (!this.selectedDate || !this.selectedDoctorId) return;

    try {
      const response = await axios.get(`http://localhost:8080/api/hospital/doctors/${this.selectedDoctorId}/available-dates/${this.selectedDate}/time-slots`);
      this.availableTimeSlots = response.data; // Assuming the backend returns a list of time slots
    } catch (error) {
      console.error('Error fetching available time slots:', error);
      alert('Failed to fetch available time slots. Please try again later.');
    }
  }

  // Book an appointment
  async onBookAppointment() {

    const userId = localStorage.getItem('userId'); // Retrieve the userId from localStorage
    if (!userId) {
      alert('User not logged in. Please log in again.');
      return;
    }
    if (this.selectedDoctorId && this.selectedDate && this.selectedTime) {
      const appointmentData = {
        patient: {
          patientId: parseInt(userId, 10)
          // patientId: parseInt(patientId, 10) // Convert patientId to a number
        },
        doctor: {
          doctorId: this.selectedDoctorId,
          date: this.selectedDate
        },
        appointmentTime: this.selectedTime,
        status: 'SCHEDULED' // Set the initial status of the appointment
      };

      try {
        const response = await axios.post('http://localhost:8080/api/hospital/appointments/book', appointmentData);
        alert('Appointment booked successfully!');
        console.log('Appointment response:', response.data);
      } catch (error) {
        console.error('Error booking appointment:', error);
        alert('Failed to book appointment. Please try again later.');
      }
    } else {
      alert('Please fill in all fields to book an appointment.');
    }
  }
}
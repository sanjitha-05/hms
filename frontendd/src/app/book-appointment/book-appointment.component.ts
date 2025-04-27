import { Component, OnInit } from '@angular/core';
import axios from 'axios';
import { Toast } from 'ngx-toastr';

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
      const token = localStorage.getItem('token'); 
      console.log('JWT Token:', token);
      const response = await axios.get('http://localhost:8080/api/hospital/doctors/getall',
        {
          headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json', 
          },
        }
      );
      console.log('Doctors Response:', response.data); 
      

      const allDoctors = response.data; 
  
      
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

 
  async onDoctorChange() {
    if (!this.selectedDoctorId) return;

    try {
      const token = localStorage.getItem('token'); // Retrieve the JWT token from localStorage

      const response = await axios.get(`http://localhost:8080/api/hospital/doctors/${this.selectedDoctorId}/available-dates`,{
        headers: {
          Authorization: `Bearer ${token}`, 
          'Content-Type': 'application/json',
        },
      });

      const allDates = response.data; 
      console.log(allDates)
      const currentDate = new Date();
      const currentDateString = currentDate.toISOString().split('T')[0]; // Get today's date in 'YYYY-MM-DD' format

      this.availableDates = allDates.filter((date: string) => {
        return date >= currentDateString; 
      });
      this.availableTimeSlots = []; 
      this.selectedDate = ''; 
    } catch (error) {
      console.error('Error fetching available dates:', error);
      alert('Failed to fetch available dates. Please try again later.');
    }
  }

  
  async onDateChange() {
    if (!this.selectedDate || !this.selectedDoctorId) return;

    try {
      const token = localStorage.getItem('token'); // Retrieve the JWT token from localStorage

      const response = await axios.get(`http://localhost:8080/api/hospital/doctors/${this.selectedDoctorId}/available-dates/${this.selectedDate}/time-slots`,
        {
          headers: {
            Authorization: `Bearer ${token}`, // Add the token to the Authorization header
            'Content-Type': 'application/json',
          },
        }
      );
      const allTimeSlots = response.data; // Assuming the backend returns a list of time slots in 'HH:mm' format
      console.log('Available Time Slots:', allTimeSlots);
      const currentDate = new Date();
      const selectedDate = new Date(this.selectedDate);

      if (selectedDate.toDateString() === currentDate.toDateString()) {
        const currentTime = currentDate.getHours() * 60 + currentDate.getMinutes(); // Current time in minutes
  
        this.availableTimeSlots = allTimeSlots
          .filter((slot: any) => {
            const time = slot.timeSlot;
            if (typeof time !== 'string') {
              console.error('Invalid time format:', time);
              return false;
            }
  
            const [hours, minutes] = time.split(':').map(Number);
            const timeInMinutes = hours * 60 + minutes;
            return timeInMinutes > currentTime && !slot.blocked;
          })
          .map((slot: any) => slot.timeSlot); 
      } else {
        this.availableTimeSlots = allTimeSlots;
      }
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
        const token = localStorage.getItem('token'); // Retrieve the JWT token from localStorage
        console.log(appointmentData)
        const response = await axios.post('http://localhost:8080/api/hospital/appointments/book', appointmentData,{
          headers:{
            Authorization:`Bearer ${token}`,
            'Content-Type': 'application/json',
          },
        });
       // this.toastr.success('Appointment booked successfully!', 'Success'); // Replace alert with a toast message

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
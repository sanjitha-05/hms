import { Component, OnInit } from '@angular/core';
import { AppointmentService } from '../services/appointment.service';

@Component({
  selector: 'app-my-appointments',
  standalone:false,
  templateUrl: './my-appointments.component.html',
  styleUrls: ['./my-appointments.component.css']
})
export class MyAppointmentsComponent implements OnInit {
  patientId: number = 0; // Patient ID from localStorage
  appointments: any[] = []; // List of appointments
  errorMessage: string = '';

  constructor(private appointmentService: AppointmentService) {}

  ngOnInit(): void {
    // Get patient ID from localStorage
    const storedPatientId = localStorage.getItem('userId');
    if (storedPatientId) {
      this.patientId = parseInt(storedPatientId, 10);
      this.fetchAppointments();
    }
  }

  // Fetch all appointments for the patient
  async fetchAppointments() {
    try {
      this.appointments = await this.appointmentService.getAppointmentsByPatientId(this.patientId);
    } catch (error) {
      console.error('Error fetching appointments:', error);
      this.errorMessage = 'Failed to fetch appointments. Please try again later.';
    }
  }

  // Placeholder for rescheduling an appointment
  onReschedule(appointmentId: number) {
    alert(`Reschedule appointment with ID: ${appointmentId}`);
    // Add logic to navigate to the reschedule page
  }

  // Placeholder for canceling an appointment
  onCancel(appointmentId: number) {
    alert(`Cancel appointment with ID: ${appointmentId}`);
    // Add logic to call the cancel appointment API
  }
}
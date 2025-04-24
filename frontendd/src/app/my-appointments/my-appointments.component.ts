import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AppointmentService } from '../services/appointment.service';

@Component({
  selector: 'app-my-appointments',
  standalone:false,
  templateUrl: './my-appointments.component.html',
  styleUrls: ['./my-appointments.component.css']
})
export class MyAppointmentsComponent implements OnInit {
  patientId: number = 0;
  appointments: any[] = [];
  errorMessage: string = '';
  appointmentId:number=0;

  constructor(private appointmentService: AppointmentService, private router: Router) {}

  ngOnInit(): void {
    const storedPatientId = localStorage.getItem('userId');
    if (storedPatientId) {
      this.patientId = parseInt(storedPatientId, 10);
      this.fetchAppointments();
    }
  }

  
  async fetchAppointments() {
    try {
      this.appointments = await this.appointmentService.getAppointmentsByPatientId(this.patientId);
      
    } catch (error) {
      console.error('Error fetching appointments:', error);
      this.errorMessage = 'Failed to fetch appointments. Please try again later.';
    }
  }

  async onReschedule(appointmentId: number) {
    const confirmation = confirm('Are you sure you want to reschedule this appointment?');
    if (confirmation) {
      try {
        const response = await this.appointmentService.cancelAppointment(appointmentId);
        alert(response);
        this.router.navigate(['/book-appointment']);
      } catch (error) {
        console.error('Error rescheduling appointment:', error);
        alert('Failed to reschedule the appointment. Please try again later.');
      }
    }
  }

  async onCancel(appointmentId: number) {
    const confirmation = confirm('Are you sure you want to cancel this appointment?');
    if (confirmation) {
      try {
        const response = await this.appointmentService.cancelAppointment(appointmentId);
        alert(response); 
        this.fetchAppointments(); 
      } catch (error) {
        console.error('Error canceling appointment:', error);
        alert('Failed to cancel the appointment. Please try again later.');
      }
    }
  }
}
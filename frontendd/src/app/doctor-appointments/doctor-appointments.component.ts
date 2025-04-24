import { Component, OnInit } from '@angular/core';
import axios from 'axios';
import { PrescriptionDialogComponent } from '../prescription-dialog/prescription-dialog.component';
import { MatDialog } from '@angular/material/dialog';


@Component({
  selector: 'app-doctor-appointments',
  standalone:false,
  templateUrl: './doctor-appointments.component.html',
  styleUrls: ['./doctor-appointments.component.css']
})
export class DoctorAppointmentsComponent implements OnInit {
  doctorId: number | null = null;
  appointments: any[] = [];
  pastAppointments: any[] = [];
  upcomingAppointments: any[] = [];
  errorMessage: string = '';

  constructor(private dialog: MatDialog) {
    // Retrieve the doctor ID from localStorage
    const storedDoctorId = localStorage.getItem('doctorId');
    if (storedDoctorId) {
      this.doctorId = parseInt(storedDoctorId, 10);
    }
  }

  ngOnInit(): void {
    if (this.doctorId) {
      this.fetchAppointments();
    } else {
      this.errorMessage = 'Doctor ID not found. Please log in again.';
    }
  }


  async fetchAppointments() {
    try {
      const token=localStorage.getItem('token')
      const response = await axios.get(`http://localhost:8080/api/hospital/appointments/getbydoctor/${this.doctorId}`,
        {
          headers: {
            Authorization: `Bearer ${token}`, // Add the token to the Authorization header
            'Content-Type': 'application/json',
          },
        }
      );
      const currentDateTime = new Date();

      
      response.data.forEach((appointment: any) => {
        const appointmentDateTime = new Date(`${appointment.doctor.date}T${appointment.appointmentTime}`); // Combine date and time

        const appointmentData = {
          appointmentId: appointment.appointmentId, 
          date: appointment.doctor.date,
          time: appointment.appointmentTime,
          patientId: appointment.patient.patientId,
          patientName: appointment.patient.name,
          status: appointment.status
        };
        if (appointmentDateTime < currentDateTime) {
          this.pastAppointments.push(appointmentData);
        } else {
          this.upcomingAppointments.push(appointmentData);
        }
      });
    } catch (error) {
      console.error('Error fetching appointments:', error);
      this.errorMessage = 'Failed to fetch appointments. Please try again later.';
    }
  }

  openPrescriptionPopup(appointment: any): void {
    const dialogRef = this.dialog.open(PrescriptionDialogComponent, {
      width: '600px',
      height: '400px',
      data: {
        patientId: appointment.patientId,
        date: appointment.date
      }
    });
    dialogRef.afterClosed().subscribe(async (result) => {
      if (result) {
        try {
          const token=localStorage.getItem('token')
          const requestBody = {
            patient: {
              patientId: result.patientId 
            },
            diagnosis: result.diagnosis,
            treatment: result.treatment,
            dateOfVisit: `${appointment.date}T${appointment.time}`
          };
          const response = await axios.post('http://localhost:8080/api/hospital/history/save', requestBody,
            {
              headers: {
                Authorization: `Bearer ${token}`,
                'Content-Type': 'application/json',
              },
            }
          );
          alert('Prescription added successfully!');
          const statusUpdateResponse = await axios.patch(
            `http://localhost:8080/api/hospital/appointments/put/${appointment.appointmentId}/status`,
            'COMPLETED',
            {
              headers: {
                Authorization: `Bearer ${token}`,
                'Content-Type': 'text/plain',
              },
            }
          );
         
          appointment.status = 'COMPLETED';

        } catch (error) {
          console.error('Error saving prescription:', error);
          alert('Failed to save prescription. Please try again.');
        }
      }
    });
  }
}
import { Injectable } from '@angular/core';
import axios from 'axios';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AppointmentService {

  private baseUrl = 'http://localhost:8080/api/hospital/appointments';

  constructor() {}

  async getAppointmentsByPatientId(patientId: number): Promise<any[]> {
    try {
      const token=localStorage.getItem('token')
      const response = await axios.get<any[]>(`${this.baseUrl}/patient/${patientId}`,
        {
          headers: {
            Authorization: `Bearer ${token}`, 
            'Content-Type': 'application/json',
          },
        }
      );
      return response.data;
    } catch (error) {
      console.error('Error fetching appointments:', error);
      throw error; 
    }
  }

  async cancelAppointment(appointmentId: number): Promise<string> {
    try {
      const token=localStorage.getItem('token')
      const response = await axios.put(`${this.baseUrl}/${appointmentId}/cancel`,
        {
          headers: {
            Authorization: `Bearer ${token}`, 
            'Content-Type': 'application/json',
          },
        }
      );
      return response.data; 
    } catch (error) {
      console.error('Error canceling appointment:', error);
      throw error;
    }
  }
}

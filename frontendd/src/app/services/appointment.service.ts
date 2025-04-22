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
      const response = await axios.get<any[]>(`${this.baseUrl}/patient/${patientId}`);
      return response.data; // Return the data from the response
    } catch (error) {
      console.error('Error fetching appointments:', error);
      throw error; // Rethrow the error for the caller to handle
    }
  }

  async cancelAppointment(appointmentId: number): Promise<string> {
    try {
      const response = await axios.put(`${this.baseUrl}/${appointmentId}/cancel`);
      return response.data; // Return the success message
    } catch (error) {
      console.error('Error canceling appointment:', error);
      throw error;
    }
  }
}

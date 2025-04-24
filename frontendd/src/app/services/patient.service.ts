import { Injectable } from '@angular/core';
import axios from 'axios';

@Injectable({
  providedIn: 'root'
})
export class PatientService {
  private baseUrl = 'http://localhost:8080/api/patient';

  constructor() {}

  // Fetch patient details by ID
  async getPatientDetails(patientId: number): Promise<any> {
    try {
      const token=localStorage.getItem('token')
      const response = await axios.get(`${this.baseUrl}/${patientId}`,
        {
          headers: {
            Authorization: `Bearer ${token}`, // Add the token to the Authorization header
            'Content-Type': 'application/json',
          },
        }
      );
      return response.data; // Return the patient details
    } catch (error) {
      console.error('Error fetching patient details:', error);
      throw error; // Rethrow the error for the caller to handle
    }
  }
}
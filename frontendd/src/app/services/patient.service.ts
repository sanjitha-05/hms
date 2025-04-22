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
      const response = await axios.get(`${this.baseUrl}/${patientId}`);
      return response.data; // Return the patient details
    } catch (error) {
      console.error('Error fetching patient details:', error);
      throw error; // Rethrow the error for the caller to handle
    }
  }
}
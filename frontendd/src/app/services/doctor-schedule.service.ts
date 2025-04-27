import { Injectable } from '@angular/core';
import axios from 'axios';
import { Observable, from } from 'rxjs';
import { map } from 'rxjs/operators';
import { defer } from 'rxjs';
import { tap } from 'rxjs/operators';

interface LocalDate {
  year: number;
  monthValue: number;
  dayOfMonth: number;
}

@Injectable({
  providedIn: 'root'
})
export class DoctorScheduleService {
  private baseUrl = 'http://localhost:8080/api/hospital/doctors';

  constructor() {}

  async getAvailableDates(doctorId: number): Promise<any> {
    const token = localStorage.getItem('token');

    try {
      const response = await axios.get(`${this.baseUrl}/${doctorId}/available-dates`,
        {
        headers: {
          Authorization: `Bearer ${token}`,
          'Content-Type': 'application/json'
        }
      }
    );
  
      return response.data; 
  
    } catch (error) {
      console.error('Error fetching available dates:', error);
      throw error; 
    }
  }
  
  async getFreeDates(doctorId: number): Promise<any> {
    const token = localStorage.getItem('token');

    try {
      const response = await axios.get(`${this.baseUrl}/${doctorId}/free-dates`,
        {
        headers: {
          Authorization: `Bearer ${token}`,
          'Content-Type': 'application/json'
        }
      }
    );
  
      return response.data; 
  
    } catch (error) {
      console.error('Error fetching available dates:', error);
      throw error; 
    }
  }
  

  async blockDate(doctorId: number, date: LocalDate): Promise<any> {
    const token = localStorage.getItem('token'); 
    try {
      const response = await axios.post(`${this.baseUrl}/${doctorId}/block-date`, 
        date ,
        {
          headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json'
          }
        }
      );
  
      console.log('Date blocked successfully:', response.data);
      return response.data; 
    } catch (error) {
      console.error('Error blocking date:', error);
      throw error; 
    }
  }

  async getBlockedDates(doctorId: number): Promise<any> {
    const token = localStorage.getItem('token');
    try {
      const response = await axios.get(`${this.baseUrl}/${doctorId}/blocked-dates`, 
        {
         headers: {
          Authorization: `Bearer ${token}`, 'Content-Type': 'application/json'
          }
         });
         console.log(response.data)
      return response.data;
    } catch (error) {
      console.error('Error fetching blocked dates:', error);
      throw error;
    }  
      
  }
    

  async unblockDate(doctorId: number, date: LocalDate): Promise<any> {
    const token = localStorage.getItem('token');
    try {
      const response = await axios.put(`${this.baseUrl}/unblock/${doctorId}/${date}`, {},
         { 
        // Empty body for PUT
        headers: {
          Authorization: `Bearer ${token}`,
          'Content-Type': 'application/json'
        }
      });
      console.log('Date unblocked successfully:', response.data);
      return response.data;
    } catch (error) {
      console.error('Error unblocking date:', error);
      throw error;
    }
  }

  // Helper function to format LocalDate for the backend URL
  private formatDateForBackend(date: LocalDate): string {
    const year = date.year;
    const month = date.monthValue < 10 ? `0${date.monthValue}` : `${date.monthValue}`;
    const day = date.dayOfMonth < 10 ? `0${date.dayOfMonth}` : `${date.dayOfMonth}`;
    return `<span class="math-inline">\{year\}\-</span>{month}-${day}`;
  }
  

  // Add similar methods for unblocking dates, and blocking/unblocking time slots
}
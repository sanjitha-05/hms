import { Component, OnInit } from '@angular/core';
import axios from 'axios';
import { Router } from '@angular/router';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-notification',
  standalone:false,
  templateUrl: './notifications.component.html',
  styleUrls: ['./notifications.component.css']
})
export class NotificationComponent implements OnInit {
  notifications: any[] = []; 
  patientNotifications: any[] = [];
  doctorNotifications: any[] = [];
  userType: string = ''; 

  constructor(private router: Router,private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe((params) => {

      this.userType = params['userType'];

      if (this.userType === 'patient' && params['userId']) {
        this.fetchPatientNotifications(params['userId']);
      } else if (this.userType === 'doctor' && params['doctorId']) {
        this.fetchDoctorNotifications(params['doctorId']);
      } else {
        alert('Invalid user type or missing ID.');
      }
    });
  }

  async fetchPatientNotifications(userId: string) {
    try {
      const token=localStorage.getItem('token')
      const response = await axios.get(`http://localhost:8080/api/hospital/notifications/patient/${userId}`,
        {
          headers: {
            Authorization: `Bearer ${token}`, 
            'Content-Type': 'application/json',
          },
        }
      );
      this.notifications = response.data; 
      this.patientNotifications = this.notifications.filter((notification: any) =>
        notification.message.startsWith('Dear')
      );
    } catch (error) {
      console.error('Error fetching patient notifications:', error);
      alert('Failed to fetch patient notifications. Please try again later.');
    }
  }
  async fetchDoctorNotifications(doctorId: number) {
    try {
      const token=localStorage.getItem('token')
      const response = await axios.get(`http://localhost:8080/api/hospital/notifications/doctor/${doctorId}`,
        {
          headers: {
            Authorization: `Bearer ${token}`, // Add the token to the Authorization header
            'Content-Type': 'application/json',
          },
        }
      );
      this.notifications = response.data; 
      this.doctorNotifications = this.notifications.filter((notification: any) =>
        notification.message.startsWith('Hi')
      );
    } catch (error) {
      console.error('Error fetching doctor notifications:', error);
      alert('Failed to fetch doctor notifications. Please try again later.');
    }
  }
}
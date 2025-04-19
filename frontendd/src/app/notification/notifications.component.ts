import { Component, OnInit } from '@angular/core';
import axios from 'axios';

@Component({
  selector: 'app-notification',
  standalone:false,
  templateUrl: './notifications.component.html',
  styleUrls: ['./notifications.component.css']
})
export class NotificationComponent implements OnInit {
  notifications: any[] = []; // Array to store notifications
  patientNotifications: any[] = [];

  constructor() {}

  ngOnInit(): void {
    // Load static notifications
    this.fetchNotifications();
  }

  // Load static notifications
  async fetchNotifications() {
    const userId = localStorage.getItem('userId'); // Retrieve the patient ID from localStorage
  if (!userId) {
    alert('User not logged in. Please log in again.');
    return;
  }
    try {
      const response = await axios.get(`http://localhost:8080/api/hospital/notifications/patient/${userId}`);
      this.notifications = response.data; // Assign the fetched data to the array
      this.patientNotifications = this.notifications.filter((notification: any) =>
        notification.message.startsWith('Dear')
      );
    } catch (error) {
      console.error('Error fetching notifications:', error);
      alert('Failed to fetch notifications. Please try again later.');
    }
  }
}
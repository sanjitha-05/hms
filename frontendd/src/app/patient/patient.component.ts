import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-patient',
  standalone:false,
  templateUrl: './patient.component.html',
  styleUrls: ['./patient.component.css']
})
export class PatientComponent {
  constructor(private router: Router) {}

  onBookAppointment() {
    // Navigate to the book appointment page
    this.router.navigate(['/book-appointment']);
  }

  onViewMedicalHistory() {
    // Navigate to the medical history page
    this.router.navigate(['/medical-history']);
  }

  onLogout() {
    // Navigate to the login page
    this.router.navigate(['/login']);
  }

  onAbout() {
    // Navigate to the about page (or display a modal)
    alert('About: This is a Hospital Management System.');
  }

  onContact() {
    // Navigate to the contact page (or display a modal)
    alert('Contact Us: Email us at support@hospital.com or call +1-800-123-4567.');
  }
}
